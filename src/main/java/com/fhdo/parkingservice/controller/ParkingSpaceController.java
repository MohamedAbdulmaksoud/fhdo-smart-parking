package com.fhdo.parkingservice.controller;

import com.fhdo.parkingservice.model.dtos.AvailableParkingSpacesResponse;
import com.fhdo.parkingservice.service.ParkingSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/parking-lots")
@RequiredArgsConstructor
public class ParkingSpaceController {

    private final ParkingSpaceService service;

    @GetMapping("/spaces/{parkingId}")
    public AvailableParkingSpacesResponse getAvailableSpaces(@PathVariable("parkingId") String parkingId){
        return service.getAvailableSpaces(parkingId);
    }
}
