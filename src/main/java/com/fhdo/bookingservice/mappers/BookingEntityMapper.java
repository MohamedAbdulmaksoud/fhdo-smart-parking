package com.fhdo.bookingservice.mappers;

import com.fhdo.bookingservice.dtos.BookingValidationRequest;
import com.fhdo.bookingservice.entities.BookingEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingEntityMapper {
    BookingValidationRequest bookingToValidationRequest(BookingEntity bookingEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    BookingEntity update(BookingValidationRequest bookingValidationRequest, @MappingTarget BookingEntity bookingEntity);
}