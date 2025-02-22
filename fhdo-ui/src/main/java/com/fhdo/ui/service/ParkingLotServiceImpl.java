package com.fhdo.ui.service;

import com.fhdo.ui.dto.NearbyParkingRequest;
import com.fhdo.ui.dto.NearbyParkingResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Profile("!dev")
public class ParkingLotServiceImpl implements ParkingLotService {
    private final RestTemplate restTemplate;

    public ParkingLotServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<NearbyParkingResponse> findNearbyParking(NearbyParkingRequest request) {
        String endpoint = "/api/v1/parking-lots/nearby";
        return restTemplate.postForObject(endpoint, request, List.class);
    }
}
