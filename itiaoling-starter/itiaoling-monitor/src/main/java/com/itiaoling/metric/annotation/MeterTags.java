package com.itiaoling.metric.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * micrometer 使用的标签
 *
 * @author gary fu
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MeterTags {

    /**
     * 标签的集合
     */
    MeterTag[] value();
}
