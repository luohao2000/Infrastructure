package com.itiaoling.log.aop.param.handler.print;

import com.itiaoling.log.aop.param.template.ParamLogCommonPartTemplate;
import com.itiaoling.log.aop.param.template.ParamLogOutputTemplate;
import com.itiaoling.log.aop.param.wrapper.ProxyMethodInvocationWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认后置日志输出处理器
 *
 * @author charles
 * @date 2023/10/12
 */
@Slf4j
public class DefaultParamLogPostPrintHandler extends ParamLogPostPrintAbstractHandler {
    public DefaultParamLogPostPrintHandler(ParamLogCommonPartTemplate headerTemplate, ParamLogOutputTemplate responseTemplate) {
        super(headerTemplate, responseTemplate);
    }

    @Override
    public void handle(ProxyMethodInvocationWrapper wrapper) {
        String message = "DefaultParamLog post log:\n" + commonPartTemplate.print(wrapper) + outputTemplate.print(wrapper);
        if (wrapper.isSuccess()) {
            log.info(message);
        } else {
            log.info(message, wrapper.getException());
        }
    }
}
