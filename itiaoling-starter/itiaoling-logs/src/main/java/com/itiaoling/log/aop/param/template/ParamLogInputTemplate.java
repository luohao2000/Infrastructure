package com.itiaoling.log.aop.param.template;

/**
 * @author charles
 * @date 2023/10/12
 */
public interface ParamLogInputTemplate extends ParamLogTemplate{

    /**
     * 给出请求参数模板
     * @return 请求参数模板
     */
    String inputTemplate();
}
