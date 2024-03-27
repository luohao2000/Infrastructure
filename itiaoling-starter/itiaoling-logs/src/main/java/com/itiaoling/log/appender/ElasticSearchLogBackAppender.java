package com.itiaoling.log.appender;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import com.itiaoling.log.date.DateTimeFormatEnum;
import com.itiaoling.log.date.DateUtil;
import com.itiaoling.log.properties.ItiaolingLogsProperties;
import com.itiaoling.log.spi.LogProviderManager;
import com.itiaoling.log.spi.log.LogPattern;
import com.itiaoling.log.tag.ElasticSearchTagHolder;
import com.itiaoling.log.utils.LogThreadPool;
import com.itiaoling.log.utils.RuntimeUtil;
import org.slf4j.MDC;
import org.springframework.util.ObjectUtils;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

/**
 * elk 日志输出器
 *
 * @author gary fu , charles , zhangyihao
 */
public class ElasticSearchLogBackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    public static final String IS_UNDEFINED = "IS_UNDEFINED";
    public static final String POSTIVE_NUMBER = "^[1-9]\\d*$";
    /**
     * 场景 用于区分不同的业务场景
     */
    private String scenario;
    /**
     * broker 地址
     */
    private String broker;
    /**
     * lingerMs
     */
    private String lingerMs;
    /**
     * batchSize
     */
    private String batchSize;
    /**
     * 业务类前缀
     */
    private String bizClassPrefix;
    /**
     * 应用名字
     */
    private String appName;
    /**
     * trace id key
     */
    private String traceIdKey;
    /**
     * message 编码器
     */
    private Encoder<ILoggingEvent> encoder;
    /**
     * 用于处理日志组件自身输出的日志信息 防止递归
     */
    private final ConcurrentLinkedDeque<ILoggingEvent> queue = new ConcurrentLinkedDeque<>();

    @Override
    public void start() {
        boolean ready = prepareForStart();
        if (!ready) {
            return;
        }

        LogLevelFilter filter = new LogLevelFilter();
        filter.start();
        addFilter(filter);

        startLogThread();

        startLogEncoder();

        super.start();
    }


    /**
     * 启动前预检测
     *
     * @return 是否准备好
     */
    private boolean prepareForStart() {
        // logback 加载是 AOT 的
        if (broker == null || broker.endsWith(IS_UNDEFINED)) {
            addWarn("No \"" + BOOTSTRAP_SERVERS_CONFIG + " and broker\" set for the appender named [\""
                    + name + "\"].");
            return false;
        }
        if (scenario == null || scenario.endsWith(IS_UNDEFINED)) {
            addWarn("No scenario set for the appender named [\"" + name + "\"].");
            return false;
        }
        if (appName == null || appName.endsWith(IS_UNDEFINED)) {
            addWarn("No appName set for the appender named [\"" + name + "\"].");
            return false;
        }

        // logback 加载比 spring 其他应用早
        ItiaolingLogsProperties instance = ItiaolingLogsProperties.getInstance();
        ItiaolingLogsProperties.AppenderProperties appender = instance.getAppender();
        ItiaolingLogsProperties.ElkAppender elkAppender = appender.getElk();

        // 存储 logback 预先获取的 spring 配置
        RuntimeUtil.setAppName(appName);
        appender.setScenario(scenario);
        elkAppender.setKafkaHost(broker);
        if (batchSize != null && !batchSize.endsWith(IS_UNDEFINED)) {
            if (batchSize.matches(POSTIVE_NUMBER)) {
                elkAppender.setBatchSize(Integer.valueOf(batchSize));
            }
        }
        if (lingerMs != null && lingerMs.endsWith(IS_UNDEFINED)) {
            if (lingerMs.matches(POSTIVE_NUMBER)) {
                elkAppender.setLingerMs(Integer.valueOf(lingerMs));
            }
        }

        // 默认使用 patternLayoutEncoder
        if (encoder == null) {
            PatternLayoutEncoder patternLayoutEncoder = new PatternLayoutEncoder();
            patternLayoutEncoder.setPattern(LogPattern.DEFAULT);
            patternLayoutEncoder.setContext(context);
            this.setEncoder(patternLayoutEncoder);
        }

        // bizClassPrefix 默认为 com.itiaoling
        if (bizClassPrefix == null) {
            bizClassPrefix = "com.itiaoling";
        }

        return true;
    }

    /**
     * 启动日志线程
     */
    private void startLogThread() {
        LogThreadPool.getThreadPoolExecutor().execute(() -> {
            // 元数据拉取成功后开始执行
            try {
                LogProviderManager.get(broker).getCountDownLatch().await();
            } catch (InterruptedException e) {
                addWarn("log thread error", e);
            }

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // ignore
                }
                ILoggingEvent logSelfEvent;
                while ((logSelfEvent = queue.poll()) != null) {
                    try {
                        log(logSelfEvent);
                    } catch (Exception e) {
                        addWarn("log error", e);
                    }
                }
            }
        });
    }


    /**
     * 启动日志编码器
     */
    private void startLogEncoder() {
        if (!encoder.isStarted()) {
            encoder.start();
        }
    }

    @Override
    public void stop() {
        super.clearAllFilters();
        super.stop();
    }


    @Override
    protected void append(ILoggingEvent loggingEvent) {
        if (!isStarted()) {
            return;
        }

        if (isLogSelf(loggingEvent)) {
            stash(loggingEvent);
        } else {
            log(loggingEvent);
        }
    }

    /**
     * 缓存日志
     *
     * @param loggingEvent 日志事件
     */
    private void stash(ILoggingEvent loggingEvent) {
        queue.add(loggingEvent);
    }

    /**
     * 判断是否是组件自身日志
     *
     * @param loggingEvent 日志事件
     * @return 是否是组件自身日志
     */
    private boolean isLogSelf(ILoggingEvent loggingEvent) {
        String loggerName = loggingEvent.getLoggerName();
        boolean isFromItiaolingLogs = loggerName.startsWith("com.itiaoling.log");
        boolean isFromKafka = loggerName.startsWith("org.apache.kafka");
        return isFromItiaolingLogs || isFromKafka;
    }

    /**
     * 记录日志
     *
     * @param loggingEvent 日志事件
     */
    private void log(ILoggingEvent loggingEvent) {
        try {
            loggingEvent.prepareForDeferredProcessing();
            //获取 应用名字
            ElasticSearchTagHolder.customIndexTags.putTag("appName", appName);

            //获取IP地址
            String ip = InetAddress.getLocalHost().getHostAddress();
            ElasticSearchTagHolder.customIndexTags.putTag("ip", ip);

            ElasticSearchTagHolder.customIndexTags.putTag("timestamp", DateUtil.getNowOfString(DateTimeFormatEnum.YYYY_MM_DDHHmmssSSS));
            ElasticSearchTagHolder.customIndexTags.putTag("reqTime", DateUtil.getNowOfString(DateTimeFormatEnum.ISO8601_YYYY_MM_DD_T_HH_mm_ss_SSS_Z));

            ElasticSearchTagHolder.customIndexTags.putTag("level", loggingEvent.getLevel().levelStr);
            ElasticSearchTagHolder.customIndexTags.putTag("thread_name", loggingEvent.getThreadName());

            StackTraceElement[] cda = loggingEvent.getCallerData();
            if (cda != null && cda.length > 0) {

                StackTraceElement firstStackTraceElement = cda[0];
                String className = firstStackTraceElement.getClassName();
                ElasticSearchTagHolder.customIndexTags.putTag("class", className);
                ElasticSearchTagHolder.customIndexTags.putTag("method", firstStackTraceElement.getMethodName());
                ElasticSearchTagHolder.customIndexTags.putTag("line", Integer.toString(firstStackTraceElement.getLineNumber()));

                if (className.startsWith(bizClassPrefix)) {
                    ElasticSearchTagHolder.customIndexTags.putTag("is_business_log", "true");
                } else {
                    ElasticSearchTagHolder.customIndexTags.putTag("is_business_log", "false");
                }
            }

            if (loggingEvent.getThrowableProxy() != null) {
                ElasticSearchTagHolder.customIndexTags.putTag("error", ThrowableProxyUtil.asString(loggingEvent.getThrowableProxy()));
            }
            if (!ObjectUtils.isEmpty(traceIdKey)) {
                String traceId = MDC.get(traceIdKey);
                ElasticSearchTagHolder.customIndexTags.putTag("trace_id", traceId);
            }

            Map<String, String> customTags = ElasticSearchTagHolder.customIndexTags.getTags();
            Map<String, String> defaultTags = ElasticSearchTagHolder.defaultIndexTags.getTags();
            String message = new String(encoder.encode(loggingEvent), StandardCharsets.UTF_8);
            LogProviderManager.get(broker).log(scenario, message, customTags, defaultTags);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ElasticSearchTagHolder.customIndexTags.clearTags();
        }
    }


    /**
     * 设置encoder
     *
     * @param encoder encoder
     */
    public void setEncoder(Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }

    /**
     * 设置应用名字
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 设置场景
     */
    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    /**
     * 设置业务类前缀
     */
    public void setBizClassPrefix(String bizClassPrefix) {
        this.bizClassPrefix = bizClassPrefix;
    }

    /**
     * 设置broker
     */
    public void setBroker(String broker) {
        this.broker = broker;
    }

    /**
     * 获取traceIdKey
     *
     * @return traceIdKey
     */
    public String getTraceIdKey() {
        return traceIdKey;
    }

    /**
     * 设置traceIdKey
     *
     * @param traceIdKey traceIdKey
     */
    public void setTraceIdKey(String traceIdKey) {
        this.traceIdKey = traceIdKey;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public void setLingerMs(String lingerMs) {
        this.lingerMs = lingerMs;
    }
}
