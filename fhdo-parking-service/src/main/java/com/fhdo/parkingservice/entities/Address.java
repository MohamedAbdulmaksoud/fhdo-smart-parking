package com.fhdo.parkingservice.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Embeddable
@Data
public class Address {

    private String streetName;

    @Positive
    @Nullable
    private Integer buildingNumber;

    private String municipality;

    @Pattern(regexp = "\\d{5}")
    private String zipCode;

    private String city;
}
