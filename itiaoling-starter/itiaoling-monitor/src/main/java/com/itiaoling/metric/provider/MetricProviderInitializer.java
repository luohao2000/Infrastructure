package com.itiaoling.metric.provider;


import com.itiaoling.metric.spec.Metric;
import io.micrometer.prometheus.PrometheusMeterRegistry;

/**
 * 编程式服务提供者
 *
 * @author gary fu
 */
public class MetricProviderInitializer {

    private final static PrometheusMetricProvider PROVIDER = new PrometheusMetricProvider();

    /**
     * 获取默认的监控指标提供者
     *
     * @return 监控指标提供者
     */
    public static Metric getInstance() {
        return PROVIDER;
    }

    /**
     * 允许修改内置的监控指标注册器
     *
     * @param registry 监控指标注册器
     */
    public static void setPrometheusMeterRegistry(PrometheusMeterRegistry registry) {
        PROVIDER.setRegistry(registry);
    }
}
