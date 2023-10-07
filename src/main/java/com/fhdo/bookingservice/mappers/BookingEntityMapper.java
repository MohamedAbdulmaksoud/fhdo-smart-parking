package com.fhdo.bookingservice.mappers;

import com.fhdo.bookingservice.domain.request.BookingConfirmationMessageRequest;
import com.fhdo.bookingservice.domain.response.BookingConfirmationResponse;
import com.fhdo.bookingservice.entities.BookingEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingEntityMapper {
    BookingConfirmationMessageRequest bookingToConfirmationRequest(BookingEntity bookingEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BookingEntity update(BookingConfirmationResponse bookingValidationRequest, @MappingTarget BookingEntity bookingEntity);
}