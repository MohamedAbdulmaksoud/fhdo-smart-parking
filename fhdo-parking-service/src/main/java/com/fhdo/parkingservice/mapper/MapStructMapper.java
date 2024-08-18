package com.fhdo.parkingservice.mapper;

import com.fhdo.parkingservice.entities.ParkingLotEntity;
import com.fhdo.parkingservice.model.dtos.NearbyParkingResponse;
import org.mapstruct.*;

import java.time.LocalTime;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class MapStructMapper {

    @Mapping(target = "isOpen", ignore = true)
    abstract NearbyParkingResponse parkingLotEntityToNearbyParkingResponse(ParkingLotEntity parkingLotEntity);

    public List<NearbyParkingResponse> parkingLotEntityListToNearbyParkingResponseList(List<ParkingLotEntity> entities) {
        return entities.stream().map(this::parkingLotEntityToNearbyParkingResponse).toList();
    }

    @AfterMapping
    void calculateIsOpen(ParkingLotEntity parkingLotEntity, @MappingTarget NearbyParkingResponse response) {
        LocalTime openingTime = parkingLotEntity.getOpeningTime();
        LocalTime closingTime = parkingLotEntity.getClosingTime();

        LocalTime currentTime = LocalTime.now();

        boolean isOpen = !currentTime.isBefore(openingTime) && !currentTime.isAfter(closingTime);

        response.setIsOpen(isOpen);
    }
}
