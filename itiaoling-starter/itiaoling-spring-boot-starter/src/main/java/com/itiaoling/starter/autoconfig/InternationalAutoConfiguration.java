package com.itiaoling.starter.autoconfig;

import com.itiaoling.starter.message.MessageSourceHandler;
import com.itiaoling.starter.properties.MessageProperties;
import jakarta.annotation.Resource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * i18n国际化配置
 *
 * @author charles
 */
@Configuration
public class InternationalAutoConfiguration {

    @Resource
    MessageProperties messageProperties;

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean(name = "customMessageSourceHandler")
    public MessageSourceHandler customMessageSourceHandler(MessageSource messageSource) {
        return new MessageSourceHandler(messageSource, messageProperties.getDefaultLocale().toString());
    }
}
