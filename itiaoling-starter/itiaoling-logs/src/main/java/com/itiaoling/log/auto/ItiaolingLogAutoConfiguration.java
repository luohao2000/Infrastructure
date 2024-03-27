package com.itiaoling.log.auto;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.itiaoling.log.aop.param.handler.ParamLogPostHandler;
import com.itiaoling.log.aop.param.handler.ParamLogPreHandler;
import com.itiaoling.log.aop.param.handler.print.DefaultParamLogPostPrintHandler;
import com.itiaoling.log.aop.param.handler.print.DefaultParamLogPrePrintHandler;
import com.itiaoling.log.aop.param.handler.print.ParamLogPostPrintHandler;
import com.itiaoling.log.aop.param.handler.print.ParamLogPrePrintHandler;
import com.itiaoling.log.aop.param.interceptor.ParamLogInterceptor;
import com.itiaoling.log.aop.param.matcher.ParamLogClassMatcher;
import com.itiaoling.log.aop.param.matcher.ParamLogMethodMatcher;
import com.itiaoling.log.aop.param.template.*;
import com.itiaoling.log.properties.ItiaolingLogsProperties;
import com.itiaoling.log.properties.ParamLogProperties;
import com.itiaoling.log.utils.RuntimeUtil;
import com.itiaoling.log.utils.SpringContextUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * itiaoling 日志自动配置类
 * 装配全局 properties
 *
 * @author charles
 * @date 2023/10/10
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({ItiaolingLogsProperties.class, ParamLogProperties.class})
public class ItiaolingLogAutoConfiguration {

    @Resource
    ApplicationContext applicationContext;

    @Resource
    Environment environment;

    @Resource
    ItiaolingLogsProperties itiaolingLogsProperties;

    /**
     * 系统层面开启了日志加载
     */
    @Bean
    @ConditionalOnProperty(prefix = "itiaoling.log.param-log", name = "enable", havingValue = "true", matchIfMissing = true)
    public Advisor paramLogAdvisor() {
        log.info("itiaolingLogAutoConfiguration paramLogAdvisor");
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        // 切点设置
        ComposablePointcut composablePointcut = new ComposablePointcut(new ParamLogMethodMatcher());
        composablePointcut.union(new ParamLogClassMatcher());
        advisor.setPointcut(composablePointcut);

        // 切面设置
        Map<String, ParamLogPreHandler> preHandlerMap = applicationContext.getBeansOfType(ParamLogPreHandler.class);
        log.info("preHandler loaded: {}", JSON.toJSONString(preHandlerMap.keySet()));
        Map<String, ParamLogPostHandler> postHandlerMap = applicationContext.getBeansOfType(ParamLogPostHandler.class);
        log.info("postHandler loaded: {}", JSON.toJSONString(postHandlerMap.keySet()));
        advisor.setAdvice(new ParamLogInterceptor(Lists.newArrayList(preHandlerMap.values()), Lists.newArrayList(postHandlerMap.values())));
        return advisor;
    }

    @Bean
    @ConditionalOnMissingBean(ParamLogPrePrintHandler.class)
    public ParamLogPrePrintHandler paramLogPreHandler() {
        log.info("itiaolingLogAutoConfiguration defaultParamLogPrePrintHandler");
        return new DefaultParamLogPrePrintHandler(paramLogHeaderTemplate(), paramLogRequestTemplate());
    }

    @Bean
    @ConditionalOnMissingBean(ParamLogPostPrintHandler.class)
    public ParamLogPostPrintHandler paramLogPostHandler() {
        log.info("itiaolingLogAutoConfiguration defaultParamLogPostPrintHandler");
        return new DefaultParamLogPostPrintHandler(paramLogHeaderTemplate(), paramLogResponseTemplate());
    }

    @Bean
    @ConditionalOnMissingBean(ParamLogCommonPartTemplate.class)
    public ParamLogCommonPartTemplate paramLogHeaderTemplate() {
        log.info("itiaolingLogAutoConfiguration defaultParamLogHeaderTemplate");
        return new DefaultParamLogCommonPartTemplate(itiaolingLogsProperties.getParamLog());
    }

    @Bean
    @ConditionalOnMissingBean(ParamLogInputTemplate.class)
    public ParamLogInputTemplate paramLogRequestTemplate() {
        log.info("itiaolingLogAutoConfiguration defaultParamLogRequestTemplate");
        return new DefaultParamLogInputTemplate(itiaolingLogsProperties.getParamLog());
    }

    @Bean
    @ConditionalOnMissingBean(ParamLogOutputTemplate.class)
    public ParamLogOutputTemplate paramLogResponseTemplate() {
        log.info("itiaolingLogAutoConfiguration defaultParamLogResponseTemplate");
        return new DefaultParamLogOutputTemplate(itiaolingLogsProperties.getParamLog());
    }

    @PostConstruct
    public void post() {
        SpringContextUtil.setApplicationContext(applicationContext);
        SpringContextUtil.setEnvironment(environment);

        RuntimeUtil.setLoaded(true);
        RuntimeUtil.setFramework(RuntimeUtil.Framework.SPRING);
        RuntimeUtil.setAppName(environment.getProperty("spring.application.name"));
    }
}
