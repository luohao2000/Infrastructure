package com.itiaoling.metric.constants;

import lombok.AllArgsConstructor;

/**
 * 指标后缀名称
 *
 * @author charles
 * @since 2024/1/9
 */
@AllArgsConstructor
public enum MeterSuffixName {
    /**
     * counter 后缀
     */
    COUNTER("count"),
    /**
     * timer 后缀
     */
    TIMER("record"),

    /**
     * gauge 后缀
     */
    TIME_GAUGE("duration"),
    ;

    public final String suffix;


}
