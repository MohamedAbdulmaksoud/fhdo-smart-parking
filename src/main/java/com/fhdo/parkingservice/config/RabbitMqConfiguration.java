package com.fhdo.parkingservice.config;

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

    public static final String CONFIRM_BOOKING_QUEUE = "booking.confirm.request";
    public static final String CONFIRM_BOOKING_RESPONSE_QUEUE = "order.confirm.response";
    public static final String CANCEL_BOOKING_QUEUE = "booking.cancel.request";
    public static final String CANCEL_BOOKING_RESPONSE = "booking.cancel.response";


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
    public Queue confirmBookingQueue() {
        return new Queue(CONFIRM_BOOKING_QUEUE);
    }

    @Bean
    public Queue confirmBookingResponse() {
        return new Queue(CONFIRM_BOOKING_RESPONSE_QUEUE);
    }

    @Bean
    public Queue cancelBookingQueue() {
        return new Queue(CANCEL_BOOKING_QUEUE);
    }

    @Bean
    public Queue cancelBookingResponse() {
        return new Queue(CANCEL_BOOKING_RESPONSE);
    }
}
