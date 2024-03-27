package com.itiaoling.log.aop.param.handler;

import com.itiaoling.log.aop.param.wrapper.ProxyMethodInvocationWrapper;

/**
 * 日志处理器
 *
 * @author charles
 * @date 2023/10/12
 */
public interface ParamLogHandler {

    /**
     * 处理日志
     *
     * @param wrapper 日志
     */
    void handle(ProxyMethodInvocationWrapper wrapper);
}
