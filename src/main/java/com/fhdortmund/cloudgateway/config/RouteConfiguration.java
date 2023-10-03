package com.fhdortmund.cloudgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!local-discovery")
@Configuration
public class RouteConfiguration {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route(r -> r.path("/api/v1/bookings/*").uri("http://localhost:8081"))
                .route(r -> r.path("/api/v1/vehicle-registration/*").uri("http://localhost:8082"))
                .route(r -> r.path("/api/v1/parking-lots/*").uri("http://localhost:8083"))
                .build();
    }
}
