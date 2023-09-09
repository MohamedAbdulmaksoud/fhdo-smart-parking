package com.fhdo.parkingservice.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Geolocation {

    private Double latitude;

    private Double longitude;
}
