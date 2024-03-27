package com.itiaoling.metric.provider;

import com.alibaba.fastjson2.JSON;
import com.itiaoling.metric.HexUtils;
import com.itiaoling.metric.constants.MeterSuffixName;
import com.itiaoling.metric.spec.Metric;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.TimeGauge;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * prometheus 编程式服务提供者
 *
 * @author gary fu
 */
public class PrometheusMetricProvider extends DotMetricNameCapable implements Metric {

    /**
     * 公共的监控指标
     */
    protected PrometheusMeterRegistry registry;

    /**
     * gauge 强引用
     */
    private final Map<String, AtomicInteger> timeGaugeReferenceMap = new ConcurrentHashMap<>();

    /**
     * 无参构造生成默认的监控指标
     */
    PrometheusMetricProvider() {
        this.registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    void setRegistry(PrometheusMeterRegistry registry) {
        this.registry = registry;
    }


    /**
     * Counter是一个常规计数器，用于对某项指标值进行累加或者递减操作
     *
     * @param metricName 指标名称
     * @param desc       描述
     * @param tagsList   标签
     */
    @Override
    public void counter(String metricName, String desc, List<Tag> tagsList) {
        Assert.hasText(metricName, "metricName must not be empty");

        Counter counter = Counter.builder(makeMetricSuffix(metricName, MeterSuffixName.COUNTER.suffix))
                .tags(tagsList)
                .description(desc)
                .register(registry);
        counter.increment();
    }

    @Override
    public void timer(String metricName, String description, List<Tag> tagList, Long duration, TimeUnit unit, double... publishPercentiles) {
        Assert.hasText(metricName, "metricName must not be empty");
        Assert.notNull(unit, "unit must not be null");
        Assert.isTrue(duration != null && duration >= 0, "duration must be greater than 0");

        Timer timer = Timer.builder(makeMetricSuffix(metricName, MeterSuffixName.TIMER.suffix))
                .tags(tagList)
                .description(description)
                .publishPercentiles(publishPercentiles)
                .publishPercentileHistogram(publishPercentiles.length > 0)
                .register(registry);

        timer.record(duration, unit);
    }

    @Override
    public void timeGauge(String metricName, String description, List<Tag> tagList, Long duration) {
        timeGauge(metricName, description, tagList, duration, TimeUnit.MILLISECONDS);
    }

    @Override
    public void timeGauge(String metricName, String description, List<Tag> tagList, Long duration, TimeUnit unit) {
        Assert.hasText(metricName, "metricName must not be empty");
        Assert.notNull(unit, "unit must not be null");
        Assert.isTrue(duration != null && duration >= 0, "duration must be greater than 0");

        String uniqueKey = HexUtils.getHexCode(metricName, description, JSON.toJSONString(tagList));
        AtomicInteger timeGauge = timeGaugeReferenceMap.computeIfAbsent(uniqueKey, key -> {
            AtomicInteger atomicInteger = new AtomicInteger();
            TimeGauge.builder(makeMetricSuffix(metricName, MeterSuffixName.TIME_GAUGE.suffix), atomicInteger, unit, AtomicInteger::get)
                    .tags(tagList)
                    .register(registry);
            return atomicInteger;
        });
        timeGauge.set(duration.intValue());
    }
}
