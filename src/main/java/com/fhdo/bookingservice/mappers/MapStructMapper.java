package com.fhdo.bookingservice.mappers;

import com.fhdo.bookingservice.domain.request.BookingConfirmationRequest;
import com.fhdo.bookingservice.domain.response.BookingConfirmationResponse;
import com.fhdo.bookingservice.entities.BookingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MapStructMapper {

    BookingConfirmationRequest createConfirmationRequestFromBooking(BookingEntity bookingEntity);

    @Mapping(target = "state", ignore = true)
    @Mapping(target = "bookingExtensions", ignore = true)
    @Mapping(target = "bookingDuration", ignore = true)
    @Mapping(target = "baseCost", ignore = true)
    @Mapping(target = "totalPenalties", ignore = true)
    @Mapping(target = "totalCost", ignore = true)
    @Mapping(target = "metadata", ignore = true)
    BookingEntity updateBookingFromResponse(BookingConfirmationResponse response, @MappingTarget BookingEntity bookingEntity);


}
