package com.itiaoling.log.aop.param.matcher;

import com.itiaoling.log.annotation.ParamClassLog;
import org.springframework.aop.ClassFilter;

/**
 * 类注解匹配器
 * @author charles
 * @date 2023/10/11
 */
public class ParamLogClassMatcher implements ClassFilter {
    @Override
    public boolean matches(Class<?> clazz) {
        // 后续补充逻辑

        // 暂时全部抓取
        return true;
    }
}
