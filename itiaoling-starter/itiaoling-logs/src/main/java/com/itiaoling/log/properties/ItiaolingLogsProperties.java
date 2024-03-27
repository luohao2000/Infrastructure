package com.itiaoling.log.properties;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * itiaoling-logs 配置项
 *
 * @author charles
 * @date 2023/10/11
 */
@ConfigurationProperties(prefix = "itiaoling.logs", ignoreInvalidFields = true)
public class ItiaolingLogsProperties {
    /**
     * spring 容器中的实例
     */
    private static ItiaolingLogsProperties springInstance = null;

    /**
     * 默认实例
     */
    private final static ItiaolingLogsProperties DEFAULT_INSTANCE = new ItiaolingLogsProperties();

    /**
     * 获取实例
     *
     * @return 实例
     */
    public static ItiaolingLogsProperties getInstance() {
        return springInstance == null ? DEFAULT_INSTANCE : springInstance;
    }

    @PostConstruct
    public void init() {
        springInstance = this;
    }

    /**
     * 参数输出配置
     */
    private ParamLogProperties paramLog;

    /**
     * appender 配置
     */
    private final AppenderProperties appender = new AppenderProperties();


    public AppenderProperties getAppender() {
        return appender;
    }

    public ParamLogProperties getParamLog() {
        return paramLog;
    }

    public void setParamLog(ParamLogProperties paramLog) {
        this.paramLog = paramLog;
    }

    public static class AppenderProperties {

        /**
         * 业务线划分 如 spk-cn，ef-cn
         */
        private String scenario = "default";

        /**
         * elk 日志输出
         */
        private final ElkAppender elk = new ElkAppender();

        public String getScenario() {
            return scenario;
        }

        public void setScenario(String scenario) {
            this.scenario = scenario;
        }

        public ElkAppender getElk() {
            return elk;
        }
    }

    public abstract static class BaseAppender {

        /**
         * Whether to start the container automatically on startup.
         */
        private boolean autoStartup = true;

        public boolean isAutoStartup() {
            return this.autoStartup;
        }

        public void setAutoStartup(boolean autoStartup) {
            this.autoStartup = autoStartup;
        }

    }

    public static class ElkAppender extends BaseAppender {
        /**
         * kafka地址
         */
        private String kafkaHost = "kafka-test.itiaoling.com:9092";

        /**
         * 发送日志间隔时间 ms 默认 2500ms 暂时无法修改
         * 这个参数用于设置生产者在发送一批消息之前等待更多消息加入批次的最长时间。如果已经达到linger.ms指定的延迟时间，即使缓冲区没有满，生产者也会立即发送批次。
         */
        private Integer lingerMs = 2500;

        /**
         * 日志满多少条发送 默认 20000 暂时无法修改
         * 这个参数用于设置生产者在发送一批消息之前等待更多消息加入批次的缓冲区大小。如果缓冲区已满，即使没有达到linger.ms指定的延迟时间，生产者也会立即发送批次。
         */
        private Integer batchSize = 20000;

        // getter setter

        public String getKafkaHost() {
            return kafkaHost;
        }

        public void setKafkaHost(String kafkaHost) {
            this.kafkaHost = kafkaHost;
        }

        public Integer getLingerMs() {
            return lingerMs;
        }

        public void setLingerMs(Integer lingerMs) {
            this.lingerMs = lingerMs;
        }

        public Integer getBatchSize() {
            return batchSize;
        }

        public void setBatchSize(Integer batchSize) {
            this.batchSize = batchSize;
        }
    }
}
