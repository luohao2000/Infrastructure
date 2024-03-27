package com.itiaoling.log.annotation;

import java.lang.annotation.*;

/**
 * 参数日志注解 用于方法
 *
 * @author gary Fu
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamMethodLog {

    /**
     * 需要脱敏的字段
     */
    String[] desensitizedFields() default {};
}
