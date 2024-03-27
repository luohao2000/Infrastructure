package com.itiaoling.log.aop.param.template;

import com.alibaba.fastjson2.JSON;
import com.itiaoling.log.annotation.ParamMethodLog;
import com.itiaoling.log.aop.param.wrapper.ProxyMethodInvocationWrapper;
import com.itiaoling.log.properties.ParamLogProperties;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author charles
 * @date 2023/10/12
 */
public class DefaultParamLogInputTemplate extends LogParamAbstractTemplate implements ParamLogInputTemplate {
    public DefaultParamLogInputTemplate(ParamLogProperties paramLogProperties) {
        super(paramLogProperties);
    }

    @Override
    public String inputTemplate() {
        StringBuilder msg = new StringBuilder();
        msg.append("invoke start time :%s\n");
        msg.append("parameter names: %s\n");
        msg.append("parameter values: %s\n");
        return msg.toString();
    }

    @Override
    public String print(ProxyMethodInvocationWrapper proxyMethodInvocationWrapper) {
        Method method = proxyMethodInvocationWrapper.getMethod();
        Object[] arguments = proxyMethodInvocationWrapper.getArguments();
        ParamMethodLog annotation = method.getAnnotation(ParamMethodLog.class);

        String invokeStartTime = formatTimeWithDefault(proxyMethodInvocationWrapper.getInvokeStartTime());
        String parameterNames = JSON.toJSONString(Arrays.stream(method.getParameters()).map(Parameter::getType).map(Class::getName).collect(Collectors.toList()));
        String parameterValues = toJsonWithDesensitization(arguments, annotation);
        return String.format(inputTemplate(), invokeStartTime, parameterNames, parameterValues);
    }
}
