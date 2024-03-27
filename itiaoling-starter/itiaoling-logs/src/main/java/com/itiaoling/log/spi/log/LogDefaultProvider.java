package com.itiaoling.log.spi.log;

import com.itiaoling.log.spi.LogProvider;

import java.util.Map;

/**
 * 默认日志输出提供者
 *
 * @author gary fu
 */
public class LogDefaultProvider implements LogProvider {

    @Override
    public void log(String scenario, String msg, Map<String, String> customerTags, Map<String, String> storedTags) {
        // disposal
    }
}
