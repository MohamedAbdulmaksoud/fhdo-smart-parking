package com.fhdo.parkingservice.controller;

import com.fhdo.parkingservice.model.dtos.NearbyParkingResponse;
import com.fhdo.parkingservice.service.ParkingLotService;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parking-lots")
@Validated
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotService service;

    @GetMapping("/nearby")
    public List<NearbyParkingResponse>  getNearbyParkings(@RequestParam("longitude") @DecimalMin("-180.0") @DecimalMax("180.0") double longitude,
                                                         @RequestParam("latitude") @DecimalMin("-90.0") @DecimalMax("90.0") double latitude,
                                                         @RequestParam("distanceInMeters") @Positive double distanceInMeters){
        return service.findNearbyParking(longitude, latitude, distanceInMeters);
    }
}
