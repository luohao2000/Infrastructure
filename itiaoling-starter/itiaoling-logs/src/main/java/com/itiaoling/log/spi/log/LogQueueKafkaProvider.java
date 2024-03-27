package com.itiaoling.log.spi.log;

import com.google.common.collect.Maps;
import com.itiaoling.json.JsonUtil;
import com.itiaoling.log.properties.ItiaolingLogsProperties;
import com.itiaoling.log.spi.LogProvider;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

/**
 * kafka日志输出提供者
 *
 * @author gary fu
 */
public class LogQueueKafkaProvider implements LogProvider {
    private static final Logger LOG = LoggerFactory.getLogger(LogQueueKafkaProvider.class);

    public static final String SCENARIO_KEY = "scenario";
    /**
     * 代理的消息生产者
     */
    private final KafkaProducer<String, String> producer;
    /**
     * 消息key
     */
    private final static String MESSAGE_KEY = "message";

    /**
     * kafka key
     */
    private final static String LOG_KEY = "log";

    /**
     * 信号灯 用于控制日志自身组件输出的日志消费
     */
    private final CountDownLatch latch = new CountDownLatch(1);

    public LogQueueKafkaProvider() {
        ItiaolingLogsProperties instance = ItiaolingLogsProperties.getInstance();
        ItiaolingLogsProperties.AppenderProperties appender = instance.getAppender();
        ItiaolingLogsProperties.ElkAppender elkAppender = appender.getElk();

        Properties props = new Properties();
        // broker 地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, elkAppender.getKafkaHost());
        // 间隔 5 秒发送
        props.put(ProducerConfig.LINGER_MS_CONFIG, elkAppender.getLingerMs());
        // 批量发送大小
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, elkAppender.getBatchSize());


        // ack 模式 leader 收到即可
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        // 不重试
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        // 获取元数据最大闲置时间
        props.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 1000 * 3600 * 10);
        // 缓存大小
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // 缓冲区阻塞等待时间 60 秒
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 1000 * 60);
        // 元数据最长有效时间 1 小时
        props.put(ProducerConfig.METADATA_MAX_AGE_CONFIG, 1000 * 60 * 60);
        // 序列化器
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 反序列化器
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // client id
        String hostAddress = "ip-unknown-";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress() + "-";
        } catch (UnknownHostException e) {
            // ignore
        }
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "itiaoling-logs-" + hostAddress + "logback-relaxed");
        producer = new KafkaProducer<>(props);
    }


    @Override
    public void log(String scenario, String message, Map<String, String> customerTags, Map<String, String> storedTags) {
        try {
            Map<String, String> tagsMap = Maps.newHashMap();
            tagsMap.putAll(customerTags);
            tagsMap.putAll(storedTags);
            KafkaMessage kafkaMessage = new KafkaMessage(message, scenario, tagsMap);
            String finalMessage = kafkaMessage.toString();
            // 已激活日志线程消费自身日志
            if (latch.getCount() <= 0) {
                producer.send(new ProducerRecord<>(scenario, LOG_KEY, finalMessage));
                return;
            }

            Future<RecordMetadata> future = producer.send(new ProducerRecord<>(scenario, LOG_KEY, finalMessage));
            RecordMetadata recordMetadata = future.get();
            if (recordMetadata != null && latch.getCount() > 0) {
                latch.countDown();
            }
        } catch (Exception e) {
            System.out.println("LogQueueKafkaProvider log Exception : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public CountDownLatch getCountDownLatch() {
        return latch;
    }

    /**
     * kafka消息对象
     *
     * @param message  日志主体
     * @param scenario 业务场景
     * @param tags     附带信息
     */
    public record KafkaMessage(String message, String scenario, Map<String, String> tags) {

        @Override
        public String toString() {
            Map<String, String> infoMap = new HashMap<>(2 << 4);
            infoMap.putAll(tags);
            infoMap.put(MESSAGE_KEY, message);
            infoMap.put(SCENARIO_KEY, scenario);
            return JsonUtil.toJson(infoMap);
        }
    }
}
