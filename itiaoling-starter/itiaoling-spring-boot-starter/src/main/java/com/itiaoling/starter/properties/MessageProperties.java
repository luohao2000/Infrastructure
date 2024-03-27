package com.itiaoling.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

/**
 * 消息相关配置
 *
 * @author charles
 * @since 2023/12/15
 */
@ConfigurationProperties(prefix = "itiaoling.message", ignoreInvalidFields = true)
public class MessageProperties  {
    /**
     * 默认语言
     */
    private Locale defaultLocale = Locale.CHINA;

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }
}
