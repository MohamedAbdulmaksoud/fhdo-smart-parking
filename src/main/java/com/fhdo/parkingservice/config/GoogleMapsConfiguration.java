package com.fhdo.parkingservice.config;

import com.google.maps.GeoApiContext;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application-secrets.properties")
public class GoogleMapsConfiguration {

    @Bean
    public GeoApiContext geoApiContext(@Value("${fhdo.api.google}") @NotBlank String apiKey) {
        GeoApiContext.Builder geoApiContextBuilder = new GeoApiContext.Builder();
        return geoApiContextBuilder.apiKey(apiKey)
                .maxRetries(5)
                .queryRateLimit(2)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
    }

}
