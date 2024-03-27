package com.itiaoling.log.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 参数日志注解 用于类上
 *
 * @author gary Fu
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamClassLog {
    /**
     * 默认值 与remark一致
     */
    @AliasFor("remark")
    String value() default "";

    /**
     * 备注 与value一致
     */
    @AliasFor("value")
    String remark() default "";
    //
    // Class<?>[] ignore() default {};

    /**
     * 排除不打印方法签名集
     */
    String[] excludeMethods() default {};
}
