package com.itiaoling.log.tag;

import org.springframework.core.task.TaskDecorator;

import java.util.concurrent.Callable;


public final class ElasticSearchLogDecorator implements TaskDecorator {

    private ElasticSearchLogDecorator() {}

    public final static ElasticSearchLogDecorator INSTANCE = new ElasticSearchLogDecorator();

    @Override
    public Runnable decorate(Runnable runnable) {
        return new ElasticSearchLogRunnable() {
            @Override
            public void doRun() {
                runnable.run();
            }
        };
    }

    public <T> Callable<T> decorate(Callable<T> callable) {
        return null;
//        return new ElasticSearchLogRunnable() {
//            @Override
//            public void doRun() throws Exception {
//                callable.call();
//            }
//
//            @Override
//            public T doCall() throws Exception {
//                return callable.call();
//            }
//        };
    }
}
