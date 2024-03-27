package com.itiaoling.log.aop.param.interceptor;

import com.google.common.collect.Lists;
import com.itiaoling.log.aop.param.handler.ParamLogPostHandler;
import com.itiaoling.log.aop.param.handler.ParamLogPreHandler;
import com.itiaoling.log.aop.param.wrapper.ProxyMethodInvocationWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ProxyMethodInvocation;

import java.util.List;
import java.util.Optional;

/**
 * 具体的切面逻辑
 *
 * @author charles
 * @date 2023/10/11
 */
@Slf4j
public class ParamLogInterceptor implements MethodInterceptor {

    /**
     * 前置处理器集
     */
    private final List<ParamLogPreHandler> paramLogPreHandlers;

    /**
     * 后置处理器集合
     */
    private final List<ParamLogPostHandler> paramLogPostHandlers;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 只有 spring 的代理才会进入这里
        if (!(invocation instanceof ProxyMethodInvocation)) {
            throw new RuntimeException("Only spring proxy method invocation is supported");
        }

        ProxyMethodInvocation proxyMethodInvocation = (ProxyMethodInvocation) invocation;
        ProxyMethodInvocationWrapper proxyMethodInvocationWrapper = new ProxyMethodInvocationWrapper(proxyMethodInvocation);
        proxyMethodInvocationWrapper.ready();

        // 前置处理
        paramLogPreHandlers.forEach(t -> {
            try {
                t.handle(proxyMethodInvocationWrapper);
            } catch (Exception e) {
                log.error("前置处理器异常", e);
            }
        });

        // 脱敏： 字段 放在配置里面
        Object result = null;
        Exception e = null;
        try {
            result = proxyMethodInvocation.proceed();
            proxyMethodInvocationWrapper.setResult(result);
        } catch (Exception exception) {
            e = exception;
            proxyMethodInvocationWrapper.setException(e);
        }

        // 后置处理
        paramLogPostHandlers.forEach(t -> {
            try {
                t.handle(proxyMethodInvocationWrapper);
            } catch (Exception exception) {
                log.error("后置处理器异常", exception);
            }
        });

        if (e != null) {
            throw e;
        }
        return result;

    }

    public ParamLogInterceptor(List<ParamLogPreHandler> paramLogPreHandlers, List<ParamLogPostHandler> paramLogPostHandlers) {
        this.paramLogPreHandlers = Optional.ofNullable(paramLogPreHandlers).orElse(Lists.newArrayList());
        this.paramLogPostHandlers = Optional.ofNullable(paramLogPostHandlers).orElse(Lists.newArrayList());
    }

    public ParamLogInterceptor() {
        this.paramLogPreHandlers = Lists.newArrayList();
        this.paramLogPostHandlers = Lists.newArrayList();
    }
}
