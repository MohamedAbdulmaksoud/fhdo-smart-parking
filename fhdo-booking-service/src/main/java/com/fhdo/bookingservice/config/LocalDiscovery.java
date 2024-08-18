package com.fhdo.bookingservice.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
/**
 * Configuration class for enabling local service discovery in the Booking Service.
 *
 * The {@code LocalDiscovery} class is a configuration component that activates local service discovery when
 * the application is running in the {@code local-discovery} profile. It uses Spring Cloud's {@code @EnableDiscoveryClient}
 * annotation to enable service registration and discovery capabilities.
 *
 * <p>This class is typically used in development or testing environments where local service discovery is required,
 * allowing the application to register with and discover other services running locally.</p>
 *
 * <p>The {@code @Profile("local-discovery")} annotation ensures that this configuration is only active when the
 * {@code local-discovery} profile is active, preventing it from being applied in other environments.</p>
 */

@Profile("local-discovery")
@EnableDiscoveryClient
@Configuration
public class LocalDiscovery {
}
