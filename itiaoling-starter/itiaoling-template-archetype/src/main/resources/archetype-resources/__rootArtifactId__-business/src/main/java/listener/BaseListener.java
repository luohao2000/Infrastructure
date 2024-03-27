#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.listener;

import ${package}.spring.exception.CustomException;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Optional;

/**
 * @author charles
 * @since 2023/12/15
 */
public abstract class BaseListener {

    /**
     * 获取deliveryTag
     * @param message 消息
     * @return deliveryTag
     */
    protected long getDeliveryTag(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        return Optional.ofNullable(deliveryTag).orElseThrow(() -> new CustomException("deliveryTag is null"));
    }
}
