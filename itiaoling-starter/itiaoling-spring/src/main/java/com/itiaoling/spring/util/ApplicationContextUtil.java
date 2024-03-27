package com.itiaoling.spring.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author gary
 */
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    private static ApplicationContextUtil applicationContextUtil;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        applicationContextUtil = this;
    }

    /**
     * 根据名称获取bean
     *
     * @param beanName bean名称
     * @param <T>      类型
     * @return bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    /**
     * 获取spring上下文
     *
     * @return spring上下文
     */
    public static ApplicationContext getContext() {
        Assert.notNull(context, "applicationContext is null");
        return context;
    }

    /**
     * 获取对象实例
     */
    public static ApplicationContextUtil get() {
        Assert.notNull(applicationContextUtil, "applicationContextUtil is null");
        Assert.notNull(context, "applicationContext is null");
        return applicationContextUtil;
    }

    /**
     * 根据名称和类型获取bean
     *
     * @param name 名称
     * @param c    类型
     * @param <T>  类型
     * @return bean
     * @throws BeansException 异常
     */
    public <T> T getBean(String name, Class<T> c) throws BeansException {
        if (!StringUtils.hasLength(name)) {
            return context.getBean(c);
        }
        return context.getBean(name, c);
    }

    /**
     * 根据类型获取bean
     *
     * @param cls 类型
     * @param <T> 类型
     * @return bean
     */
    public <T> T getBean(Class<T> cls) {
        return getBean(null, cls);
    }

    /**
     * 获取一个接口的所有实现类
     *
     * @param c   接口类
     * @param <T> 接口类型
     * @return 实现类集合
     */
    public <T> Map<String, T> getAllImplementations(Class<T> c) {
        return context.getBeansOfType(c);
    }
}
