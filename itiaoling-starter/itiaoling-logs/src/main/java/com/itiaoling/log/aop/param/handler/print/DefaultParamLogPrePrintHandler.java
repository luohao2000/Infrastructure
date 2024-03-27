package com.itiaoling.log.aop.param.handler.print;

import com.itiaoling.log.aop.param.template.ParamLogCommonPartTemplate;
import com.itiaoling.log.aop.param.template.ParamLogInputTemplate;
import com.itiaoling.log.aop.param.wrapper.ProxyMethodInvocationWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 默认前置日志输出处理器
 *
 * @author charles
 * @date 2023/10/12
 */
@Slf4j
public class DefaultParamLogPrePrintHandler extends ParamLogPrePrintAbstractHandler {

    public DefaultParamLogPrePrintHandler(ParamLogCommonPartTemplate headerTemplate, ParamLogInputTemplate requestTemplate) {
        super(headerTemplate, requestTemplate);
    }

    @Override
    public void handle(ProxyMethodInvocationWrapper wrapper) {
        log.info("DefaultParamLog previous log:\n" + commonPartTemplate.print(wrapper) + inputTemplate.print(wrapper));
    }
}
