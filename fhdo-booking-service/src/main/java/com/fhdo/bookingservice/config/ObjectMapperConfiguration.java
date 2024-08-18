package com.fhdo.bookingservice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration class for customizing the {@link ObjectMapper} used for JSON processing in the Booking Service.
 *
 * The {@code ObjectMapperConfiguration} class provides a custom {@link ObjectMapper} bean with specific configurations
 * for serializing and deserializing JSON data. This class ensures that the {@link ObjectMapper} is properly configured
 * to handle date and time formats, as well as empty values.
 *
 * <p>Configuration details:
 * <ul>
 *     <li>{@code SerializationFeature.WRITE_DATES_AS_TIMESTAMPS} is disabled to ensure that dates are serialized in
 *         ISO-8601 format rather than as timestamps.</li>
 *     <li>{@code DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT} is enabled to allow deserialization of empty
 *         arrays as {@code null} objects.</li>
 *     <li>{@code DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT} is enabled to allow deserialization of empty
 *         strings as {@code null} objects.</li>
 *     <li>{@code JavaTimeModule} is registered to support Java 8 date and time types (e.g., {@code LocalDate}, {@code LocalDateTime}).</li>
 * </ul>
 * </p>
 *
 * @see ObjectMapper
 * @see SerializationFeature
 * @see DeserializationFeature
 * @see JavaTimeModule
 */

@Configuration
public class ObjectMapperConfiguration {

    @Primary
    @Bean
    public ObjectMapper objectMapper() {

        final var om = new ObjectMapper();
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
        om.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        om.registerModule(new JavaTimeModule());

        return om;
    }
}
