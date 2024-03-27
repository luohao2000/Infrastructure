package com.itiaoling.log.aop.param.template;

import com.itiaoling.log.annotation.ParamMethodLog;
import com.itiaoling.log.aop.param.wrapper.ProxyMethodInvocationWrapper;
import com.itiaoling.log.properties.ParamLogProperties;
import com.itiaoling.log.tag.ElasticSearchTagHolder;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author charles
 * @date 2023/10/12
 */
public class DefaultParamLogOutputTemplate extends LogParamAbstractTemplate implements ParamLogOutputTemplate {
    public DefaultParamLogOutputTemplate(ParamLogProperties paramLogProperties) {
        super(paramLogProperties);
    }

    @Override
    public String outputTemplate(boolean success) {
        StringBuilder msg = new StringBuilder();
        msg.append("invoke end time :%s\n");
        msg.append("total use second :%ss\n");
        if (success) {
            msg.append("return name: %s\n");
            msg.append("return value: %s\n");
        } else {
            msg.append("exception message : %s\n");
        }
        return msg.toString();
    }

    @Override
    public String print(ProxyMethodInvocationWrapper proxyMethodInvocationWrapper) {
        Method method = proxyMethodInvocationWrapper.getMethod();
        ParamMethodLog annotation = method.getAnnotation(ParamMethodLog.class);
        String invokeEndTime = formatTimeWithDefault(proxyMethodInvocationWrapper.getInvokeEndTime());

        BigDecimal invokeEnd = BigDecimal.valueOf(proxyMethodInvocationWrapper.getInvokeEndTime());
        BigDecimal invokeStart = BigDecimal.valueOf(proxyMethodInvocationWrapper.getInvokeStartTime());
        String invokeDurationTime = invokeEnd.subtract(invokeStart).divide(BigDecimal.valueOf(1000L), 5, RoundingMode.HALF_UP)
                .stripTrailingZeros().toPlainString();

        Object[] arguments = proxyMethodInvocationWrapper.getArguments();
        String parameterValues = toJsonWithDesensitization(arguments, annotation);

        ElasticSearchTagHolder customIndexTags = ElasticSearchTagHolder.customIndexTags;
        customIndexTags.putTag("request", parameterValues);
        if (proxyMethodInvocationWrapper.isSuccess()) {
            String returnName = method.getReturnType().getName();
            Object result = proxyMethodInvocationWrapper.getResult();
            String returnValue = toJsonWithDesensitization(result, annotation);
            customIndexTags.putTag("response", returnValue);
            return String.format(outputTemplate(true), invokeEndTime,
                    invokeDurationTime,
                    returnName,
                    returnValue);
        } else {
            Exception exception = proxyMethodInvocationWrapper.getException();
            customIndexTags.putTag("brief_exception", exception.getMessage());
            return String.format(outputTemplate(false), invokeEndTime,
                    invokeDurationTime,
                    exception.getMessage()
            );
        }
    }
}
