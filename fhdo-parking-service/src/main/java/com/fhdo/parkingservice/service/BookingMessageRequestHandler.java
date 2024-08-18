package com.fhdo.parkingservice.service;

import com.fhdo.parkingservice.config.RabbitMqConfiguration;
import com.fhdo.parkingservice.entities.ParkingSpaceEntity;
import com.fhdo.parkingservice.model.BookingCancellationMessageRequest;
import com.fhdo.parkingservice.model.BookingCancellationMessageResponse;
import com.fhdo.parkingservice.model.BookingConfirmationMessageRequest;
import com.fhdo.parkingservice.model.BookingConfirmationResponse;
import com.fhdo.parkingservice.repositories.ParkingSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BookingMessageRequestHandler {

    private final RabbitTemplate rabbitTemplate;

    private final ParkingSpaceRepository repository;

    @RabbitListener(queues = RabbitMqConfiguration.CONFIRM_BOOKING_QUEUE)
    public void processConfirmationRequest(BookingConfirmationMessageRequest request) {
        Integer parkingSpotId = Optional.of(request)
                .map(BookingConfirmationMessageRequest::getParkingSpotId)
                .orElseThrow(() -> new RuntimeException("can not extract parking spot id from request"));
        UUID parkingId = Optional.of(request)
                .map(BookingConfirmationMessageRequest::getParkingId)
                .orElseThrow(() -> new RuntimeException("can not extract parking id from request"));

        ParkingSpaceEntity parkingSpot = repository.findParkingSpaceEntityByParkingIdAndInternalId(parkingId, parkingSpotId)
                .orElseThrow(() -> new RuntimeException("can not find parking space for ParkingLotId: %s, and ParkingSpotId: %s".formatted(parkingId, parkingSpotId)));

        // insert booking validation logic. Currently set to always accept.
        rabbitTemplate.convertAndSend(RabbitMqConfiguration.CONFIRM_BOOKING_RESPONSE_QUEUE, new BookingConfirmationResponse(request.getBookingId(), true));
    }

    @RabbitListener(queues = RabbitMqConfiguration.CANCEL_BOOKING_QUEUE)
    public void processCancellationRequest(BookingCancellationMessageRequest request) {
        Integer parkingSpotId = Optional.of(request)
                .map(BookingCancellationMessageRequest::getParkingSpotId)
                .orElseThrow(() -> new RuntimeException("can not extract parking spot id from request"));
        UUID parkingId = Optional.of(request)
                .map(BookingCancellationMessageRequest::getParkingId)
                .orElseThrow(() -> new RuntimeException("can not extract parking id from request"));

        ParkingSpaceEntity parkingSpot = repository.findParkingSpaceEntityByParkingIdAndInternalId(parkingId, parkingSpotId)
                .orElseThrow(() -> new RuntimeException("can not find parking space for ParkingLotId: %s, and ParkingSpotId: %s".formatted(parkingId, parkingSpotId)));

        // insert booking cancellation validation logic. Currently set to always accept.
        rabbitTemplate.convertAndSend(RabbitMqConfiguration.CANCEL_BOOKING_RESPONSE, new BookingCancellationMessageResponse(request.getBookingId(), true));
    }
}
