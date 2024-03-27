#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import ${package}.properties.MqQueueProperties;
import ${package}.log.utils.SpringContextUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author charles
 * @since 2023/12/14
 */
@Slf4j
@Configuration
public class RabbitConfig {

    @Resource
    private ConnectionFactory connectionFactory;

    @Resource
    private MqQueueProperties mqQueueProperties;


    @Bean
    public SimpleRabbitListenerContainerFactory defaultRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPrefetchCount(1);
        factory.setConcurrentConsumers(1);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息发送成功:{}", Optional.ofNullable(correlationData).map(CorrelationData::getReturned).map(JSON::toJSONString).orElse("returned is null"));
            } else {
                log.info("消息发送失败，原因：" + cause);
            }
        });
        template.setReturnsCallback(returned -> log.info("消息被退回，原因：{}", returned.getReplyText()));
        return template;
    }

    /**
     * 创建alarm-platform项目使用的消息交换器
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(mqQueueProperties.getExchangeName(), true, false);
    }

    @Bean
    @ConditionalOnProperty(prefix = "itiaoling.mq", name = "dead-letter-exchange-name")
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(mqQueueProperties.getDeadLetterExchangeName(), true, false);
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void registerMq() {
        MqQueueProperties.BindingProperties[] bindings = mqQueueProperties.getBindings();
        if (bindings == null || bindings.length == 0) {
            log.info("no mq binding config");
            return;
        }

        ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) SpringContextUtil.getApplicationContext();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) applicationContext.getBeanFactory();

        String deadLetterExchangeName = mqQueueProperties.getDeadLetterExchangeName();
        boolean hasDeadLetterExchange = deadLetterExchangeName != null;
        String exchangeName = mqQueueProperties.getExchangeName();
        for (MqQueueProperties.BindingProperties binding : bindings) {
            String bindingQueueName = binding.getBindingQueueName();
            // 创建Queue的Bean定义
            GenericBeanDefinition queueBeanDefinition = new GenericBeanDefinition();
            queueBeanDefinition.setBeanClass(Queue.class);
            queueBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(bindingQueueName);

            // 如果配置了死信队列，则将死信队列绑定到当前队列上
            if (hasDeadLetterExchange) {
                MutablePropertyValues propertyValues = queueBeanDefinition.getPropertyValues();
                Map<String, Object> o = (Map<String, Object>) propertyValues.get("arguments");
                if (o == null) {
                    o = new LinkedHashMap<>();
                    propertyValues.add("arguments", o);
                }
                o.put("x-dead-letter-exchange", deadLetterExchangeName);
                o.put("x-dead-letter-routing-key", binding.getDeadLetterRoutingKey());
            }

            // 注册Queue的Bean定义
            queueBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
            registry.registerBeanDefinition(bindingQueueName, queueBeanDefinition);


            // 创建Binding的Bean定义
            GenericBeanDefinition bindingBeanDefinition = new GenericBeanDefinition();
            bindingBeanDefinition.setBeanClass(Binding.class);
            bindingBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(bindingQueueName);
            bindingBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(Binding.DestinationType.QUEUE);
            bindingBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(exchangeName);
            bindingBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(binding.getRoutingKey());
            bindingBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(Maps.newHashMap());
            bindingBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
            // 注册Binding的Bean定义
            registry.registerBeanDefinition(bindingQueueName + "Binding", bindingBeanDefinition);
        }
    }
}