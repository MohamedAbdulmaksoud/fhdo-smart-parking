package com.fhdortmund.cloudgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("local-discovery")
@Configuration
public class LoadBalancedRoutesConfig {
    @Bean
    public RouteLocator loadBalancedRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route(r -> r.path("/api/v1/booking/*").uri("lb://booking-service"))
                .route(r -> r.path("/api/v1/vehicle-registration/*").uri("lb://vehicle-registration-service"))
                .route(r -> r.path("/api/v1/parking-lots/*").uri("lb://parking-service"))
                .build();
    }
}
