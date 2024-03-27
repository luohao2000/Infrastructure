package com.itiaoling.log.aop.param.template;

/**
 * 日志输出模板
 * @author charles
 * @date 2023/10/12
 */
public interface ParamLogCommonPartTemplate extends ParamLogTemplate{

    /**
     * 给出头部模板
     * @return 头部模板
     */
    String commonPartTemplate();
}
