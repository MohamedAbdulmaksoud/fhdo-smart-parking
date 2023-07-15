package com.fhdo.bookingservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMqConfiguration {

    public static final String CONFIRM_ORDER_QUEUE = "order.confirm.request";
    public static final String CONFIRM_ORDER_RESPONSE_QUEUE = "order.confirm.response";




/*
* Used as the default listener factory to create message listener container responsible to serve endpoints
* */
    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> containerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        MessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter(); //<x>
        factory.setMessageConverter(jackson2JsonMessageConverter);
        return factory;
    }

    @Bean
    public Queue confirmOrderQueue(){
        return new Queue(CONFIRM_ORDER_QUEUE);
    }

    @Bean
    public Queue confirmOrderResponse(){
        return new Queue(CONFIRM_ORDER_RESPONSE_QUEUE);
    }
}
