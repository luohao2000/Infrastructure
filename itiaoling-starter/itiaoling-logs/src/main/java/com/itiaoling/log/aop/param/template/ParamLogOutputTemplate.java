package com.itiaoling.log.aop.param.template;

/**
 * @author charles
 * @date 2023/10/12
 */
public interface ParamLogOutputTemplate extends ParamLogTemplate {

    /**
     * 给出响应参数模板
     *
     * @param success 是否成功
     * @return 响应参数模板
     */
    String outputTemplate(boolean success);
}
