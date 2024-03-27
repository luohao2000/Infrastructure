package com.itiaoling.metric.spi;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;

import java.util.List;

/**
 * 用于 spi 上报逻辑 push gateway 链路
 *
 * @author charles
 * @since 2023/12/27
 */
public interface MetricProvider {
    /**
     * controller 接口指标收集到 probe 本地 meterRegistry
     *
     * @param controllerMetric 接口指标
     */
    void controllerMetrics(ControllerMetric controllerMetric);

    /**
     * controller 接口指标收集到 probe 本地 meterRegistry 增加额外 tag 指定
     *
     * @param controllerMetric 接口指标
     * @param extraTags        额外的 tag
     */
    void controllerMetrics(ControllerMetric controllerMetric, List<Tag> extraTags);

    /**
     * 使用 put 方式推送到 push gateway 覆盖
     *
     * @param url push gateway url
     */
    void push(String url);

    /**
     * 使用 post 方式推送到 push gateway 累加
     *
     * @param url push gateway url
     */
    void pushAdd(String url);

    /**
     * 获取内置的 registry
     *
     * @return registry
     */
    MeterRegistry get();
}
