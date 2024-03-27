package com.itiaoling.log.aop.param.handler.print;

import com.itiaoling.log.aop.param.template.ParamLogCommonPartTemplate;
import com.itiaoling.log.aop.param.template.ParamLogOutputTemplate;

/**
 * 日志处理器
 *
 * @author charles
 * @date 2023/10/12
 */
public abstract class ParamLogPostPrintAbstractHandler extends ParamLogPrintAbstractHandler implements ParamLogPostPrintHandler {

    protected ParamLogOutputTemplate outputTemplate;

    protected ParamLogPostPrintAbstractHandler(ParamLogCommonPartTemplate headerTemplate, ParamLogOutputTemplate outputTemplate) {
        super(headerTemplate);
        this.outputTemplate = outputTemplate;
    }
}
