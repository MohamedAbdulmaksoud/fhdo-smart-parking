package com.fhdo.ui.service;

import com.fhdo.ui.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class UserService {

    @Value("${gateway.url}")
    private String gatewayUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public UUID registerUser(UserDto user) {
        String apiUrl = gatewayUrl + "/api/v1/users/register";
        UUID uuid;
        try {
            uuid = restTemplate.postForObject(apiUrl, new UserDto(null, user.getEmail(), user.getPassword()), UUID.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e.getResponseBodyAsString());
        }
        return uuid;
    }

    public UUID authenticate(String email, String password) {
        String apiUrl = gatewayUrl + "/api/v1/users/login";
        UUID uuid;
        try {
            uuid = restTemplate.postForObject(apiUrl, new UserDto(null, email, password), UUID.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e.getResponseBodyAsString());
        }
        return uuid;
    }
}
