package com.itiaoling.metric.spec;

import io.micrometer.core.instrument.Tag;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 编程式调用器接口
 * 独立接口非依赖 spring
 *
 * @author gary fu
 */
public interface Monitor {

    /**
     * 记录次数
     *
     * @param tagList 标签
     */
    void counter(List<Tag> tagList);

    /**
     * 记录耗时
     *
     * @param tagList  标签
     * @param duration 执行时间
     * @param unit     时间单位
     */
    void timer(List<Tag> tagList, Long duration, TimeUnit unit);

    /**
     * 记录调用次数与耗时
     *
     * @param tagList            标签
     * @param duration           执行时间
     * @param unit               时间单位
     * @param publishPercentiles 百分比
     */
    void record(List<Tag> tagList, Long duration, TimeUnit unit, double... publishPercentiles);

    /**
     * 记录调用次数与耗时
     *
     * @param tagList            标签
     * @param runnable           调用方法
     * @param publishPercentiles 百分比
     */
    void record(List<Tag> tagList, Runnable runnable, double... publishPercentiles);


}
