package com.itiaoling.metric.monitor;

/**
 * @author charles
 * @since 2024/1/10
 */
public interface MonitorEvent<T> {

    /**
     * 以监控的方式执行
     *
     * @return 返回执行结果
     * @throws Throwable 执行过程中的异常
     */
    T runWithMonitor() throws Throwable;
}
