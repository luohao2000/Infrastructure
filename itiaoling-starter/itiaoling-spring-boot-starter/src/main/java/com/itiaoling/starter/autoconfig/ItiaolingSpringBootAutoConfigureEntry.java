package com.itiaoling.starter.autoconfig;


import com.itiaoling.metric.monitor.MonitorExecutor;
import com.itiaoling.metric.monitor.TaggedMonitor;
import com.itiaoling.metric.spec.Monitor;
import com.itiaoling.spring.util.ApplicationContextUtil;
import com.itiaoling.spring.util.EnvironmentUtil;
import com.itiaoling.starter.aop.MonitorAspect;
import com.itiaoling.starter.filter.MonitorFilter;
import com.itiaoling.starter.properties.AppProperties;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Collections;

/**
 * @author gary
 */
@Configuration
@ComponentScan("com.itiaoling")
@EnableConfigurationProperties(AppProperties.class)
public class ItiaolingSpringBootAutoConfigureEntry {
    private static final Logger LOG = LoggerFactory.getLogger(ItiaolingSpringBootAutoConfigureEntry.class);

    @Resource
    private Environment environment;

    @Bean
    public ApplicationContextUtil applicationContextUtil() {
        return new ApplicationContextUtil();
    }

    @Bean
    public EnvironmentUtil environmentUtil() {
        return new EnvironmentUtil();
    }

    @Bean
    public Monitor taggedMonitor(PrometheusMeterRegistry prometheusMeterRegistry) {
        String applicationName = environment.getProperty("spring.application.name");
        if (applicationName == null) {
            throw new RuntimeException("spring.application.name is null");
        }
        LOG.info("taggedMonitor register as spring bean application name is {}", applicationName);

        TaggedMonitor taggedMonitor = new TaggedMonitor(applicationName);
        taggedMonitor.setPrometheusMeterRegistry(prometheusMeterRegistry);
        return taggedMonitor;
    }

    @Bean
    public MonitorExecutor monitorExecutor(Monitor taggedMonitor, AppProperties appProperties) {
        String productLine = appProperties.getProductLine();
        String dataCenter = appProperties.getDataCenter();
        if (productLine == null) {
            throw new RuntimeException("productLine is null");
        }
        if (dataCenter == null) {
            throw new RuntimeException("dataCenter is null");
        }
        LOG.info("monitor executor register as spring bean productLine is {} dataCenter is {}", productLine, dataCenter);
        return new MonitorExecutor(taggedMonitor, productLine, dataCenter);
    }

    @Bean
    @ConditionalOnProperty(name = "itiaoling.monitor.enable-aspect", havingValue = "true")
    public MonitorAspect monitorAspect() {
        LOG.info("monitor aspect register as spring bean");
        return new MonitorAspect();
    }

    @Bean
    @ConditionalOnWebApplication
    public FilterRegistrationBean<MonitorFilter> logFilterRegistration(Monitor taggedMonitor, AppProperties appProperties) {
        FilterRegistrationBean<MonitorFilter> registrationBean = new FilterRegistrationBean<>();
        MonitorFilter logFilter = new MonitorFilter(taggedMonitor, appProperties);
        registrationBean.setFilter(logFilter);
        registrationBean.setUrlPatterns(Collections.singletonList("/*"));
        return registrationBean;
    }
}