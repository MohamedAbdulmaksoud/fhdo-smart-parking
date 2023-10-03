package com.fhdo.bookingservice.entities;

import com.fhdo.bookingservice.domain.BookingState;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID bookingId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingState state;

    @Column(nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "parking_id", nullable = false, updatable = false)
    private UUID parkingId;

    @ManyToOne
    @JoinColumn(name = "parking_id", nullable = false, updatable = false, insertable = false)
    private ParkingLotEntity parkingLot;

    private Integer parkingSpotId;

    private UUID vehicleId;

    private OffsetDateTime startTime;

    private OffsetDateTime endTime;

    @Transient
    private Duration bookingDuration;

    //original cost of the booking
    private BigDecimal baseCost;

    @Transient
    private BigDecimal totalPenalties;

    //total cost incl. extensions and penalties
    private BigDecimal totalCost;

    @Embedded
    private Audit metadata;

}
