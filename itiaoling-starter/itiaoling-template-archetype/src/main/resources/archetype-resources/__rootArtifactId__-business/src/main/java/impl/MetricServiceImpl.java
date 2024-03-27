#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.impl;

import ${package}.MetricService;
import ${package}.properties.MqQueueProperties;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * @author charles
 * @since 2023/12/15
 */
@Service
public class MetricServiceImpl implements MetricService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private MqQueueProperties mqQueueProperties;

    @Override
    public void send(String message) {
        rabbitTemplate.convertAndSend(mqQueueProperties.getExchangeName(), "metric-queue-key", "hello");
    }
}
