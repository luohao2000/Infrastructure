package com.itiaoling.log.aop.param.handler.print;

import com.itiaoling.log.aop.param.handler.ParamLogPostHandler;

/**
 * 定义后置日志输出处理器
 *
 * @author charles
 * @date 2023/10/12
 */
public interface ParamLogPostPrintHandler extends ParamLogPostHandler {
    String TYPE = "postPrintHandler";

}
