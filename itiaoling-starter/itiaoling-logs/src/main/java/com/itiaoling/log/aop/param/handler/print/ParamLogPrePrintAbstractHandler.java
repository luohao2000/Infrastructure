package com.itiaoling.log.aop.param.handler.print;

import com.itiaoling.log.aop.param.template.ParamLogCommonPartTemplate;
import com.itiaoling.log.aop.param.template.ParamLogInputTemplate;

/**
 * 日志处理器
 *
 * @author charles
 * @date 2023/10/12
 */
public abstract class ParamLogPrePrintAbstractHandler extends ParamLogPrintAbstractHandler implements ParamLogPrePrintHandler {

    protected ParamLogInputTemplate inputTemplate;

    protected ParamLogPrePrintAbstractHandler(ParamLogCommonPartTemplate headerTemplate, ParamLogInputTemplate inputTemplate) {
        super(headerTemplate);
        this.inputTemplate = inputTemplate;
    }
}
