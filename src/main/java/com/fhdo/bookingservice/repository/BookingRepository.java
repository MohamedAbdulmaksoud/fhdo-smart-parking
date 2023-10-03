package com.fhdo.bookingservice.repository;

import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, UUID>
{
    @Transactional
    @Modifying
    @Query("update BookingEntity b set b.state = ?1 where b.bookingId = ?2")
    int updateStateByBookingId(@NonNull BookingState state, UUID bookingId);

}
