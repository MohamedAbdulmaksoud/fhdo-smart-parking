package com.fhdo.parkingservice.controller;

import com.fhdo.parkingservice.model.dtos.NearbyParkingRequest;
import com.fhdo.parkingservice.model.dtos.NearbyParkingResponse;
import com.fhdo.parkingservice.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parking-lots")
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotService service;

    @PostMapping("/nearby")
    public List<NearbyParkingResponse> getNearbyParkingsByGeolocation(@RequestBody NearbyParkingRequest request) {
        return service.findNearbyParking(request.origin(),
                request.destination(),
                request.distanceInMeters());
    }

    @PostMapping("/nearby/{address}")
    public List<NearbyParkingResponse> getNearbyParkingsByAddress(@RequestBody NearbyParkingRequest request) {
        return service.findNearbyParking(request.address(), request.distanceInMeters());
    }
}
