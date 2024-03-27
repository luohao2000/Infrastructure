package com.itiaoling.metric.spec;

import io.micrometer.core.instrument.Tag;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 编程式指标接口
 *
 * @author gary fu
 */
public interface Metric {

    /**
     * Counter是一个常规计数器，用于对某项指标值进行累加(不允许累减)操作，带标签
     *
     * @param metricName  指标名称
     * @param description 描述
     * @param tagList     标签
     */
    void counter(String metricName, String description, List<Tag> tagList);

    /**
     * timer 反应的是某个函数的执行时间，比如某个函数的执行时间是10ms，那么timer的值就是10ms
     *
     * @param metricName         指标名称
     * @param description        描述
     * @param tagList            标签
     * @param duration           执行时间
     * @param unit               时间单位
     * @param publishPercentiles 百分比
     */
    void timer(String metricName, String description, List<Tag> tagList, Long duration, TimeUnit unit, double... publishPercentiles);

    /**
     * timeGauge 反应的是某个函数的执行时间，比如某个函数的执行时间是10ms，那么timeGauge的值就是10ms
     *
     * @param metricName  指标名称
     * @param description 描述
     * @param tagList     标签
     * @param duration    执行时间
     */
    void timeGauge(String metricName, String description, List<Tag> tagList, Long duration);

    /**
     * timeGauge 反应的是某个函数的执行时间，比如某个函数的执行时间是10ms，那么timeGauge的值就是10ms
     *
     * @param metricName  指标名称
     * @param description 描述
     * @param tagList     标签
     * @param duration    执行时间
     * @param unit        时间单位
     */
    void timeGauge(String metricName, String description, List<Tag> tagList, Long duration, TimeUnit unit);


//
//    void resetTimer();
//
//    void meter();
//
//    void resetMeter();
//
//    /**
//     * Histogram反应的是数据流中的值的分布情况。包含最小值、最大值、平均值、中位数、p75、p90、p95、p98、p99以及p999数据分布情况。
//     */
//    void histograms();
}
