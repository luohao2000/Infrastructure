package com.itiaoling.log.aop.param.handler.print;

import com.itiaoling.log.aop.param.handler.ParamLogHandler;
import com.itiaoling.log.aop.param.template.ParamLogCommonPartTemplate;

/**
 * 日志处理器
 *
 * @author charles
 * @date 2023/10/12
 */
public abstract class ParamLogPrintAbstractHandler implements ParamLogHandler {

    protected ParamLogCommonPartTemplate commonPartTemplate;

    protected ParamLogPrintAbstractHandler(ParamLogCommonPartTemplate commonPartTemplate){
        this.commonPartTemplate = commonPartTemplate;
    }

    protected ParamLogPrintAbstractHandler(){
    }
}
