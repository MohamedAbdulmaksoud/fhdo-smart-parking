package com.fhdo.parkingservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhdo.parkingservice.model.BookingCancellationMessageRequest;
import com.fhdo.parkingservice.model.BookingCancellationMessageResponse;
import com.fhdo.parkingservice.model.BookingConfirmationMessageRequest;
import com.fhdo.parkingservice.model.BookingConfirmationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRabbit
@RequiredArgsConstructor
public class RabbitMqConfiguration {

    private final ObjectMapper objectMapper;
    public static final String CONFIRM_BOOKING_QUEUE = "booking.confirm.request";
    public static final String CONFIRM_BOOKING_RESPONSE_QUEUE = "order.confirm.response";
    public static final String CANCEL_BOOKING_QUEUE = "booking.cancel.request";
    public static final String CANCEL_BOOKING_RESPONSE = "booking.cancel.response";

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("confirmation_request", BookingConfirmationMessageRequest.class);
        idClassMapping.put("cancellation_request", BookingCancellationMessageRequest.class);
        idClassMapping.put("confirmation_response", BookingConfirmationResponse.class);
        idClassMapping.put("cancellation_response", BookingCancellationMessageResponse.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jsonConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
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
