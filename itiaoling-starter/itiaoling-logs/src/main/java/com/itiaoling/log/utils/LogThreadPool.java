package com.itiaoling.log.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 通用线程池
 *
 * @author charles
 * @date 2023/9/25
 */
public class LogThreadPool {

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    static {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                // 设置线程名格式
                .setNameFormat("itiaoling-logs-thread-%d")
                // 设置线程优先级
                .setPriority(Thread.MAX_PRIORITY)
                // 设置线程为守护线程
                .setDaemon(false)
                .build();
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
                2,
                4,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2 << 8),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return THREAD_POOL_EXECUTOR;
    }
}
