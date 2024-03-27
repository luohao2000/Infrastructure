package com.itiaoling.metric.monitor;

import com.itiaoling.metric.constants.TagName;
import com.itiaoling.metric.provider.MetricProviderInitializer;
import com.itiaoling.metric.spec.Metric;
import com.itiaoling.metric.spec.Monitor;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 编程触发式监控器
 *
 * @author gary fu
 */
public class TaggedMonitor extends AbstractMonitor implements Monitor {

    /**
     * 应用名称
     */
    private final String applicationName;

    /**
     * 监控指标提供者
     */
    private final static Metric PROVIDER;

    static {
        PROVIDER = MetricProviderInitializer.getInstance();
    }

    public TaggedMonitor(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * 线程本地变量
     */
    private final ThreadLocal<Map<String, String>> TAGS_THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

    /**
     * 变更监控指标记录器
     *
     * @param registry 监控指标记录器
     */
    public void setPrometheusMeterRegistry(PrometheusMeterRegistry registry) {
        MetricProviderInitializer.setPrometheusMeterRegistry(registry);
    }

    /**
     * 清空标签
     */
    public void clearTags() {
        TAGS_THREAD_LOCAL.get().clear();
    }

    /**
     * 携带标签
     *
     * @param name  标签名称
     * @param value 标签值
     * @return 当前监控器
     */
    public TaggedMonitor withTag(String name, String value) {
        if (value != null) {
            TAGS_THREAD_LOCAL.get().put(name, value);
        }
        return this;
    }

    /**
     * 携带标签
     *
     * @param tags 标签
     * @return 当前监控器
     */
    public TaggedMonitor withTags(Map<String, String> tags) {
        if (tags != null) {
            TAGS_THREAD_LOCAL.get().putAll(tags);
        }
        return this;
    }

    @Override
    public void counter(List<Tag> tagList) {
        List<Tag> tags = resetApplicationName(applicationName, tagList);
        transferTags(tags);
        PROVIDER.counter(applicationName, "TaggedMonitor.Counter", tagList);
    }

    @Override
    public void timer(List<Tag> tagList, Long duration, TimeUnit unit) {
        List<Tag> tags = resetApplicationName(applicationName, tagList);
        transferTags(tags);
        PROVIDER.timer(applicationName, "TaggedMonitor.Timer", tagList, duration, unit);
    }

    @Override
    public void record(List<Tag> tagList, Long duration, TimeUnit unit, double... publishPercentiles) {
        List<Tag> tags = resetApplicationName(applicationName, tagList);
        transferTags(tags);
        PROVIDER.counter(applicationName, "TaggedMonitor.Record", tagList);
        PROVIDER.timer(applicationName, "TaggedMonitor.Record", tagList, duration, unit, publishPercentiles);
    }

    @Override
    public void record(List<Tag> tagList, Runnable runnable, double... publishPercentiles) {
        Long start = System.currentTimeMillis();
        try {
            runnable.run();
        } finally {
            Long end = System.currentTimeMillis();
            record(tagList, end - start, TimeUnit.MILLISECONDS, publishPercentiles);
        }
    }

    private void transferTags(List<Tag> tagList) {
        Map<String, String> map = TAGS_THREAD_LOCAL.get();
        map.forEach((key, value) -> {
            if (key == null || value == null) {
                return;
            }

            // 优先使用传入的tagList
            for (Tag tag : tagList) {
                if (tag.getKey().equals(key)) {
                    return;
                }
            }

            // 传入的tagList中没有，就加入
            tagList.add(new ImmutableTag(key, value));
        });

        addTagIfNotExists(tagList, new ImmutableTag(TagName.SOURCE, "unsetSource"));
        addTagIfNotExists(tagList, new ImmutableTag(TagName.PRODUCT_LINE, "unsetProductLine"));
        addTagIfNotExists(tagList, new ImmutableTag(TagName.DATA_CENTER, "unsetDataCenter"));
    }

    private void addTagIfNotExists(List<Tag> tagList, Tag tag) {
        boolean hasTag = existsTag(tagList, tag.getKey());
        if (!hasTag) {
            tagList.add(tag);
        }
    }

    private static boolean existsTag(List<Tag> tagList, String tagKey) {
        boolean hasTag = false;
        for (Tag tagInList : tagList) {
            if (tagInList.getKey().equals(tagKey)) {
                hasTag = true;
                break;
            }
        }
        return hasTag;
    }
}
