package com.example.order.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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

    public static final String QUEUE_NAME = "product.queue";
    public static final String EXCHANGE_NAME = "product.exchange";
    public static final String ROUTING_KEY = "product.routing";

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
    public Queue productQueue() {
        log.debug("RabbitConfig#productQueue");
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange productExchange() {
        log.debug("RabbitConfig#productExchange");
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding binding(Queue productQueue, DirectExchange productExchange) {
        log.debug("RabbitConfig#binding");
        return BindingBuilder.bind(productQueue).to(productExchange).with(ROUTING_KEY);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public SmartInitializingSingleton declareQueuesAfterAllBeans(RabbitAdmin rabbitAdmin,
                                                                 Queue productQueue,
                                                                 DirectExchange productExchange,
                                                                 Binding binding) {
        return () -> {
            log.info("Explicitly declaring RabbitMQ resources...");
            rabbitAdmin.declareExchange(productExchange);
            rabbitAdmin.declareQueue(productQueue);
            rabbitAdmin.declareBinding(binding);
            log.info("RabbitMQ resources declared successfully");
        };
    }
}