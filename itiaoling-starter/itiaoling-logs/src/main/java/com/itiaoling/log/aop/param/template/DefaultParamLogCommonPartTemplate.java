package com.itiaoling.log.aop.param.template;

import com.itiaoling.log.aop.param.wrapper.ProxyMethodInvocationWrapper;
import com.itiaoling.log.properties.ParamLogProperties;

import java.lang.reflect.Method;

/**
 * @author charles
 * @date 2023/10/12
 */
public class DefaultParamLogCommonPartTemplate extends LogParamAbstractTemplate implements ParamLogCommonPartTemplate {
    public DefaultParamLogCommonPartTemplate(ParamLogProperties paramLogProperties) {
        super(paramLogProperties);
    }

    @Override
    public String commonPartTemplate() {
        StringBuilder msg = new StringBuilder();
        msg.append("class name: %s\n");
        msg.append("method signature: %s\n");
        return msg.toString();
    }

    @Override
    public String print(ProxyMethodInvocationWrapper proxyMethodInvocationWrapper) {
        String headerTemplate = commonPartTemplate();

        Method method = proxyMethodInvocationWrapper.getMethod();
        String className = method.getDeclaringClass().getName();
        return String.format(headerTemplate, className, method.getName());
    }
}
