package com.fhdo.parkingservice.service;

import com.fhdo.parkingservice.mapper.MapStructMapper;
import com.fhdo.parkingservice.model.dtos.NearbyParkingResponse;
import com.fhdo.parkingservice.repositories.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    private final MapStructMapper mapper;

    public List<NearbyParkingResponse> findNearbyParking(double longitude, double latitude, double distanceInMeters) {
        return mapper.parkingLotEntityListToNearbyParkingResponseList(parkingLotRepository.findNearbyParking(longitude, latitude, distanceInMeters));
    }
}
