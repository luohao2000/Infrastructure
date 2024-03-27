package com.itiaoling.log.spi.log;

/**
 * @author charles
 * @date 2023/11/28
 */
public class LogPattern {
    /**
     * 默认日志输出格式
     */
    public static final String DEFAULT = "TIME %d{yyyy-MM-dd'T'HH:mm:ss.SSS'Z'} %contextName [%thread]  -%5p [%X{X-B3-TraceId:-}] %-40.40logger{39} %L %m %n -%wEx";
}
