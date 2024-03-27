package com.itiaoling.log.spi;

import com.itiaoling.log.spi.log.LogDefaultProvider;
import com.itiaoling.log.spi.log.LogQueueKafkaProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志输出提供者管理器
 *
 * @author gary fu
 */
public class LogProviderManager {

    /**
     * 日志输出提供者容器
     */
    private final static Map<String, LogProvider> CONTAINER = new ConcurrentHashMap<>();



    /**
     * 获取日志输出提供者
     *
     * @param host kafka地址
     * @return 日志输出提供者
     */
    public static LogProvider get(String host) {
        LogProvider logProvider = CONTAINER.get(host);
        if (logProvider != null) {
            return logProvider;
        }

        synchronized (host.intern()) {
            logProvider = CONTAINER.get(host);
            if (logProvider == null) {
                try {
                    logProvider = new LogQueueKafkaProvider();
                } catch (Exception e) {
                    logProvider = new LogDefaultProvider();
                    System.out.println("create LogQueueKafkaProvider Exception : " + e.getMessage());
                }
                CONTAINER.put(host, logProvider);
            }
        }

        return logProvider;
    }
}
