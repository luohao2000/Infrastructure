package com.itiaoling.log.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author charles
 * @date 2023/10/12
 */
public class SpringContextUtil {
    private static ApplicationContext applicationContext;

    private static Environment environment;

    private SpringContextUtil() {
    }


    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Environment getEnvironment() {
        return environment;
    }

    public static void setEnvironment(Environment environment) {
        SpringContextUtil.environment = environment;
    }

    public static <T> T getBean(String name, Class<T> c) throws BeansException {
        if (!StringUtils.hasLength(name)) {
            return applicationContext.getBean(c);
        }
        return applicationContext.getBean(name, c);
    }

    public static <T> T getBean(Class<T> cls) {
        return getBean(null, cls);
    }

    public static <T> Map<String, T> getAllImplementations(Class<T> c) {
        return applicationContext.getBeansOfType(c);
    }
}
