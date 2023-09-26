package com.fhdo.bookingservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parking_lot")
public class ParkingLotEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID parkingId;

    @OneToMany(mappedBy = "parkingLot")
    private Set<BookingEntity> bookingEntity;

    private String fullName;

    @Embedded
    private Address address;

    @Embedded
    private Geolocation geoLocation;

}
