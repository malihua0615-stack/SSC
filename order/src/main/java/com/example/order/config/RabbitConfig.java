package com.example.order.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitConfig {
    /**
     * 1.队列绑定死信队列和对应的死信路由键
     * 2.声明死信队列和死信交换机
     * 3.声明死信队列和死信交换机的绑定关系
     * 4.声明队列和交换机的绑定关系
     */
    public static final String QUEUE_NAME = "order.queue";
    public static final String EXCHANGE_NAME = "order.exchange";
    public static final String ROUTING_KEY = "order.routing";

    // 死信相关
    public static final String ORDER_DLX_EXCHANGE = "order.dlx.exchange";
    public static final String ORDER_DLQ_QUEUE = "order.dlq.queue";
    public static final String ORDER_DLQ_ROUTING_KEY = "order.dlq";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public Queue orderQueue() {
        log.debug("RabbitConfig#orderQueue");
        return QueueBuilder.durable(QUEUE_NAME)
                //死信交换机
                .withArgument("x-dead-letter-exchange", ORDER_DLX_EXCHANGE)
                //死信路由键
                .withArgument("x-dead-letter-routing-key", ORDER_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue orderDlxQueue() {
        log.debug("RabbitConfig#orderDlxQueue");
        return QueueBuilder.durable(ORDER_DLQ_QUEUE).build();
    }

    @Bean
    public DirectExchange orderExchange() {
        log.debug("RabbitConfig#orderExchange");
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public DirectExchange orderDlxExchange() {
        log.debug("RabbitConfig#orderDlxExchange");
        return new DirectExchange(ORDER_DLX_EXCHANGE, true, false);
    }

    @Bean
    public Binding orderBinding() {
        log.debug("RabbitConfig#orderBinding");
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ROUTING_KEY);
    }

    @Bean
    public Binding orderDlxBinding() {
        log.debug("RabbitConfig#orderDlxBinding");
        return BindingBuilder.bind(orderDlxQueue()).to(orderDlxExchange()).with(ORDER_DLQ_ROUTING_KEY);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

//    @Bean
//    public SmartInitializingSingleton declareQueuesAfterAllBeans(RabbitAdmin rabbitAdmin,
//                                                                 Queue productQueue,
//                                                                 DirectExchange productExchange,
//                                                                 Binding binding) {
//        return () -> {
//            log.info("Explicitly declaring RabbitMQ resources...");
//            rabbitAdmin.declareExchange(productExchange);
//            rabbitAdmin.declareQueue(productQueue);
//            rabbitAdmin.declareBinding(binding);
//            log.info("RabbitMQ resources declared successfully");
//        };
//    }


}