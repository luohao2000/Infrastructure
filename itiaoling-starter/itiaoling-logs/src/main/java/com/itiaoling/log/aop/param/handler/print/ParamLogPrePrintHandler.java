package com.itiaoling.log.aop.param.handler.print;

import com.itiaoling.log.aop.param.handler.ParamLogPreHandler;

/**
 * 定义前置日志输出处理器
 *
 * @author charles
 * @date 2023/10/12
 */
public interface ParamLogPrePrintHandler extends ParamLogPreHandler {
    String TYPE = "prePrintHandler";
}
