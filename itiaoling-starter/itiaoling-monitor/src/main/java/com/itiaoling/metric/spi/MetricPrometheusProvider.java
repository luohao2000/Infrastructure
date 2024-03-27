package com.itiaoling.metric.spi;

import io.micrometer.core.instrument.*;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.PushGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author charles
 * @since 2023/12/27
 */
@Slf4j
public class MetricPrometheusProvider implements MetricProvider {

    /**
     * metric 默认 tag
     */
    public static final String APPLICATION_NAME = "applicationName";
    /**
     * registry id
     */
    private final String id;
    /**
     * 实际使用的 registry
     */
    private final PrometheusMeterRegistry registry;

    /**
     * 应用级别的耗时 gauge 强引用
     * 指向的对象被回收了会显示 NAN
     */
    private final AtomicInteger appDuration = new AtomicInteger(0);

    /**
     * 应用级别的耗时 gauge 强引用
     */
    private final Map<String, AtomicInteger> appDurationMap = new ConcurrentHashMap<>();

    /**
     * 全局耗时 gauge 强引用
     */
    private static final AtomicInteger API_DURATION = new AtomicInteger(0);


    public String getId() {
        return id;
    }

    /**
     * 默认构造器
     *
     * @param applicationName 应用名称
     */
    MetricPrometheusProvider(String applicationName) {
        this(applicationName, null);
    }

    /**
     * 构造器
     *
     * @param applicationName 应用名称
     * @param tags            公共的 tag
     */
    MetricPrometheusProvider(String applicationName, List<Tag> tags) {
        Assert.hasText(applicationName, "applicationName must not be empty");
        this.id = applicationName;
        this.registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        List<Tag> commonTags = new ArrayList<>();
        if (applicationName != null && !applicationName.isEmpty()) {
            commonTags.add(new ImmutableTag(APPLICATION_NAME, applicationName));
        }
        if (tags != null && !tags.isEmpty()) {
            commonTags.addAll(tags);
        }

        if (!commonTags.isEmpty()) {
            this.registry.config().commonTags(commonTags);
        }
    }

    /**
     * rate(counter[10s]) 查询10s内的速率
     * rate(timer_sum[10])/rate(timer_count[10s]) 查询10s内的平均耗时
     */
    @Override
    public void controllerMetrics(ControllerMetric controllerMetric) {
        controllerMetrics(controllerMetric, null);
    }

    @Override
    public void controllerMetrics(ControllerMetric controllerMetric, List<Tag> extraTags) {
        Long duration = controllerMetric.getDuration();
        Assert.notNull(duration, "duration must not be null");
        Assert.isTrue(duration >= 0, "duration must not be negative");
        int intDuration = Math.toIntExact(duration);

        List<Tag> tags = new ArrayList<>();
        tags.add(new ImmutableTag("url", controllerMetric.getUrl()));
        tags.add(new ImmutableTag("http_method", controllerMetric.getHttpMethod()));
        tags.add(new ImmutableTag("http_status", controllerMetric.getHttpStatus()));
        tags.add(new ImmutableTag("biz_code", controllerMetric.getBizCode()));
        if (extraTags != null && !extraTags.isEmpty()) {
            tags.addAll(extraTags);
        }

        // 应用级别的请求数
        Counter requestCounter = Counter.builder(controllerMetric.getAppName() + "_count")
                .description(controllerMetric.getDescription())
                .tags(tags)
                .register(this.registry);
        requestCounter.increment();

        // 应用级别的耗时
        String uniqueCode = controllerMetric.getUniqueCode();
        if (uniqueCode == null) {
            appDuration.set(intDuration);
            TimeGauge.builder(controllerMetric.getAppName() + "_duration", appDuration, TimeUnit.MILLISECONDS, AtomicInteger::get)
                    .tags(tags)
                    .register(registry);
        } else {
            AtomicInteger durationAtomicInteger = appDurationMap.computeIfAbsent(uniqueCode, k -> new AtomicInteger(0));
            durationAtomicInteger.set(intDuration);
            TimeGauge.builder(controllerMetric.getAppName() + "_duration", durationAtomicInteger, TimeUnit.MILLISECONDS, AtomicInteger::get)
                    .tags(tags)
                    .register(registry);
        }


        // 全局的请求数
        Counter allRequestCounter = Counter.builder("api_count")
                .tags(tags)
                .register(this.registry);
        allRequestCounter.increment();

        // 全局的耗时
        API_DURATION.set(intDuration);
        TimeGauge.builder("api_duration", API_DURATION, TimeUnit.MILLISECONDS, AtomicInteger::get)
                .tags(tags)
                .register(registry);
    }

    @Override
    public MeterRegistry get() {
        return this.registry;
    }

    @Override
    public void push(String url) {
        try {
            URL urlObject = new URL(url);
            PushGateway pg = new PushGateway(urlObject);
            pg.push(registry.getPrometheusRegistry(), "alarm_probe_push_job", PushGateway.instanceIPGroupingKey());
        } catch (Exception e) {
            log.error("push metrics to push gateway error", e);
        }
    }

    @Override
    public void pushAdd(String url) {
        try {
            URL urlObject = new URL(url);
            PushGateway pg = new PushGateway(urlObject);
            pg.pushAdd(registry.getPrometheusRegistry(), "alarm_probe_push_job", PushGateway.instanceIPGroupingKey());
        } catch (Exception e) {
            log.error("push metrics to push gateway error", e);
        }
    }


}