package com.itiaoling.metric.annotation;


import java.lang.annotation.*;

/**
 * 标识需要上报指标的代理类
 * 指标包含 调用次数，调用时间，调用结果
 *
 * @author gary fu
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MetricRecord {
    MeterTag[] extraTags() default {};

}
