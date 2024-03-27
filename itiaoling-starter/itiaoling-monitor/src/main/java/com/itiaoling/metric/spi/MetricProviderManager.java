package com.itiaoling.metric.spi;

import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author charles
 * @since 2023/12/27
 */
@Slf4j
public class MetricProviderManager {

    /**
     * 日志输出提供者容器
     */
    private final static Map<String, MetricProvider> CONTAINER = new ConcurrentHashMap<>();


    /**
     * 获取监控 spi
     *
     * @param applicationName 应用名称
     * @param tagList         公共的 tag
     * @return 日志输出提供者
     */
    public static MetricProvider get(String applicationName, List<Tag> tagList) {
        MetricProvider metricProvider = CONTAINER.get(applicationName);
        if (metricProvider != null) {
            return metricProvider;
        }

        synchronized (applicationName.intern()) {
            metricProvider = CONTAINER.get(applicationName);
            if (metricProvider == null) {
                metricProvider = new MetricPrometheusProvider(applicationName, tagList);
                CONTAINER.put(applicationName, metricProvider);
            }
        }
        return metricProvider;
    }

    /**
     * 获取监控 spi
     *
     * @param applicationName 应用名称
     * @return 日志输出提供者
     */
    public static MetricProvider get(String applicationName) {
        return get(applicationName, null);
    }
}
