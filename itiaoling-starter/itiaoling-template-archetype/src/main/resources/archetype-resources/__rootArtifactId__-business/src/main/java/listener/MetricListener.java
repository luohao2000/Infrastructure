#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author charles
 * @since 2023/12/14
 */
@Slf4j
@Component
public class MetricListener extends BaseListener {

    @RabbitHandler
    @RabbitListener(queues = "metric-queue", containerFactory = "defaultRabbitListenerContainerFactory", concurrency = "2")
    public void onMessage(Message<String> msg, Channel channel) throws Exception {
        long deliveryTag = getDeliveryTag(msg);
        String payload = msg.getPayload();
        try {
            log.info("[MetricListener] 开始消费监控信息：{}", payload);
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            log.error("[MetricListener]: 消息处理异常：" + payload, e);
            channel.basicAck(deliveryTag, false);
        }
    }
}
