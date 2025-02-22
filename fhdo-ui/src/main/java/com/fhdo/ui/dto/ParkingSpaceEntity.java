package com.fhdo.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpaceEntity {

    private UUID parkingSpaceId;

    private UUID parkingId;

    private ParkingLotEntity parkingSpace;

    private Integer internalId;

    private Boolean isReserved;

    private Boolean isOccupied;

    private ParkingSpotType parkingSpotType;

}
