package com.itiaoling.log.aop.param.matcher;

import com.itiaoling.log.annotation.ParamClassLog;
import com.itiaoling.log.annotation.ParamMethodLog;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;

/**
 * 方法注解匹配器
 *
 * @author charles
 * @date 2023/10/11
 */
public class ParamLogMethodMatcher implements MethodMatcher {
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        boolean hasParamLog = specificMethod.isAnnotationPresent(ParamMethodLog.class);
        ParamClassLog paramClassLog = targetClass.getAnnotation(ParamClassLog.class);
        // 没有方法注解 也没有类注解 直接返回
        if(paramClassLog == null && !hasParamLog){
            return false;
        }

        // 有方法注解 但是类注解没有
        if(paramClassLog == null){
            return true;
        }

        // 其余情况
        String[] excludeMethods = paramClassLog.excludeMethods();
        String methodName = specificMethod.getName();
        for (String excludeMethod : excludeMethods) {
            if(excludeMethod.equals(methodName)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isRuntime() {
        // 只需要静态匹配
        return false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        // isRuntime true 才会生效
        return false;
    }
}
