#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author charles
 */
@Data
@Component
@ConfigurationProperties("itiaoling.mq")
public class MqQueueProperties {
    /**
     * 交换器名称
     */
    private String exchangeName;

    /**
     * 死信交换器名称
     */
    private String deadLetterExchangeName;

    /**
     * 绑定信息
     */
    private BindingProperties[] bindings;

    /**
     * 绑定配置
     */
    @Data
    public static class BindingProperties {
        /**
         * 绑定的key
         */
        private String routingKey;
        /**
         * 绑定的队列
         */
        private String bindingQueueName;
        /**
         * 死信路由 key
         */
        private String deadLetterRoutingKey;
    }
}
