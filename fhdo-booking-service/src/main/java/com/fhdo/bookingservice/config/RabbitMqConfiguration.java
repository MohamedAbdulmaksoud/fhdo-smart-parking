package com.fhdo.bookingservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhdo.bookingservice.domain.request.BookingCancellationMessageRequest;
import com.fhdo.bookingservice.domain.request.BookingConfirmationMessageRequest;
import com.fhdo.bookingservice.domain.response.BookingCancellationMessageResponse;
import com.fhdo.bookingservice.domain.response.BookingConfirmationResponse;
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

/**
 * Configuration class for setting up RabbitMQ in the Booking Service.
 *
 * The {@code RabbitMqConfiguration} class configures RabbitMQ components required for messaging within the Booking Service.
 * It sets up queues for booking requests and responses, configures message conversion, and creates listener containers for
 * handling incoming messages.
 *
 * <p>This class provides beans for:
 * <ul>
 *     <li>Message conversion using {@link Jackson2JsonMessageConverter} and {@link ObjectMapper} for JSON processing.</li>
 *     <li>Mapping message types to Java classes using {@link DefaultClassMapper}.</li>
 *     <li>Defining RabbitMQ queues for booking confirmation and cancellation requests and responses.</li>
 *     <li>Creating a default listener factory for message handling with {@link SimpleRabbitListenerContainerFactory}.</li>
 * </ul>
 * </p>
 *
 * <p>Key Beans:
 * <ul>
 *     <li>{@code classMapper()} - Configures a {@link DefaultClassMapper} to map message types to Java classes.</li>
 *     <li>{@code rabbitTemplate(ConnectionFactory)} - Provides a {@link RabbitTemplate} configured with a JSON message converter.</li>
 *     <li>{@code jsonConverter()} - Defines a {@link Jackson2JsonMessageConverter} for converting messages to and from JSON.</li>
 *     <li>{@code containerFactory()} - Sets up a {@link SimpleRabbitListenerContainerFactory} for creating message listener containers.</li>
 *     <li>{@code confirmBookingQueue()} - Creates a {@link Queue} for booking confirmation requests.</li>
 *     <li>{@code confirmBookingResponse()} - Creates a {@link Queue} for booking confirmation responses.</li>
 *     <li>{@code cancelBookingQueue()} - Creates a {@link Queue} for booking cancellation requests.</li>
 *     <li>{@code cancelBookingResponse()} - Creates a {@link Queue} for booking cancellation responses.</li>
 * </ul>
 * </p>
 *
 * @see ObjectMapper
 * @see Jackson2JsonMessageConverter
 * @see DefaultClassMapper
 * @see RabbitTemplate
 * @see SimpleRabbitListenerContainerFactory
 * @see Queue
 * @see ConnectionFactory
 */

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
