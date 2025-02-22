package com.fhdo.ui.service;

import com.fhdo.ui.dto.NearbyParkingRequest;
import com.fhdo.ui.dto.NearbyParkingResponse;

import java.util.List;

public interface ParkingLotService {
    List<NearbyParkingResponse> findNearbyParking(NearbyParkingRequest request);
}
