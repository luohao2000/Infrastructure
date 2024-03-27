package com.itiaoling.log.aop.param.template;

import com.itiaoling.log.aop.param.wrapper.ProxyMethodInvocationWrapper;

/**
 * 注解日志输出模板
 * @author charles
 * @date 2023/10/12
 */
public interface ParamLogTemplate {
    /**
     * 日志输出模板 允许自己提供
     * @param proxyMethodInvocationWrapper 代理方法调用
     * @return 日志
     */
    String print(ProxyMethodInvocationWrapper proxyMethodInvocationWrapper);
}
