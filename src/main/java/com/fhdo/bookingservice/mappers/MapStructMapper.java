package com.fhdo.bookingservice.mappers;

import com.fhdo.bookingservice.domain.request.BookingConfirmationRequest;
import com.fhdo.bookingservice.domain.response.BookingConfirmationResponse;
import com.fhdo.bookingservice.entities.BookingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapStructMapper {

    BookingConfirmationRequest createConfirmationRequestFromBooking(BookingEntity bookingEntity);

    BookingEntity updateBookingFromResponse(BookingConfirmationResponse response, @MappingTarget BookingEntity bookingEntity);


}
