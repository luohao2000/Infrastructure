package com.itiaoling.log.aop.param.handler;

/**
 * 定义前置日志处理器
 *
 * @author charles
 * @date 2023/10/12
 */
public interface ParamLogPreHandler extends ParamLogHandler {
    String TYPE = "preHandler";
}
