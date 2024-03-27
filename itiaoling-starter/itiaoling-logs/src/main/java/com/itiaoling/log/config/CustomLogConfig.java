package com.itiaoling.log.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 用于填充 ip
 * @author charles
 * @date 2023/4/27
 */
public class CustomLogConfig extends ClassicConverter {
    private static String webIP;

    static {
        try {
            webIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("获取日志Ip异常" + e.getMessage());
            webIP = null;
        }
    }

    @Override
    public String convert(ILoggingEvent event) {
        return webIP;
    }
}

