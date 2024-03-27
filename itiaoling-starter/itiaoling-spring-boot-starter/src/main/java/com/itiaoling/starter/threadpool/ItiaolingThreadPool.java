package com.itiaoling.starter.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author charles
 * @since 2023/12/21
 */
public class ItiaolingThreadPool {
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    static {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                // 设置线程名格式
                .setNameFormat("itiaoling-thread-%d")
                // 设置线程优先级
                .setPriority(Thread.MAX_PRIORITY)
                // 设置线程为守护线程
                .setDaemon(false)
                .build();
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
                4,
                8,
                5,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(2 << 16),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return THREAD_POOL_EXECUTOR;
    }
}
