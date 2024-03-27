package com.itiaoling.metric.annotation;


import java.lang.annotation.*;

/**
 * micrometer 使用的标签
 *
 * @author gary fu
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = MeterTags.class)
public @interface MeterTag {


    /**
     * 标签的key
     */
    String key();

    /**
     * 标签的值
     */
    String value();
}
