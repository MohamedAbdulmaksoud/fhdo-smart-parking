package com.fhdo.ui.service;


import com.fhdo.ui.dto.AvailableParkingSpacesResponse;

public interface ParkingSpaceService {
    AvailableParkingSpacesResponse getAvailableSpaces(String parkingId);
}
