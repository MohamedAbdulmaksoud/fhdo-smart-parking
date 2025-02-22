package com.fhdo.ui.service;

import com.fhdo.ui.dto.AvailableParkingSpacesResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("!dev")
public class ParkingSpaceServiceImpl implements ParkingSpaceService {
    private final RestTemplate restTemplate;

    public ParkingSpaceServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public AvailableParkingSpacesResponse getAvailableSpaces(String parkingId) {
        String endpoint = "/api/v1/parking-lots/spaces/" + parkingId;
        return restTemplate.getForObject(endpoint, AvailableParkingSpacesResponse.class);
    }
}
