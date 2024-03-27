package com.itiaoling.log.aop.param.wrapper;

import org.springframework.aop.ProxyMethodInvocation;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

/**
 * 包装类 规范后续增强的方法
 *
 * @author charles
 * @date 2023/10/12
 */
public class ProxyMethodInvocationWrapper {
    public ProxyMethodInvocationWrapper(ProxyMethodInvocation proxyMethodInvocation) {
        this.proxyMethodInvocation = proxyMethodInvocation;
    }

    /**
     * 内置的代理方法调用
     */
    private final ProxyMethodInvocation proxyMethodInvocation;

    /**
     * 内置的代理方法调用结果
     */
    private Object result;

    /**
     * 内置的代理方法抛出异常
     */
    private Exception exception;

    /**
     * 调用开始时间
     */
    private long invokeStartTime;

    /**
     * 调用结束时间
     */
    private long invokeEndTime;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.invokeEndTime = System.currentTimeMillis();
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.invokeEndTime = System.currentTimeMillis();
        this.exception = exception;
    }

    public boolean isSuccess() {
        return exception == null;
    }

    public boolean isFail() {
        return exception != null;
    }

    public Method getMethod() {
        return proxyMethodInvocation.getMethod();
    }

    public Object[] getArguments() {
        return proxyMethodInvocation.getArguments();
    }

    public long getInvokeStartTime() {
        return invokeStartTime;
    }

    public long getInvokeEndTime() {
        return invokeEndTime;
    }

    /**
     * 准备就绪 记录开始时间
     */
    public void ready() {
        this.invokeStartTime = System.currentTimeMillis();
    }


}
