package com.fhdo.bookingservice.domain.sm.actions;

import com.fhdo.bookingservice.config.RabbitMqConfiguration;
import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingConfirmationMessageRequest;
import com.fhdo.bookingservice.entities.BookingEntity;
import com.fhdo.bookingservice.mappers.BookingEntityMapper;
import com.fhdo.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.fhdo.bookingservice.services.DefaultBookingService.BOOKING_ID_HEADER;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmBookingAction implements Action<BookingState, BookingEvent> {

    private final RabbitTemplate rabbitTemplate;

    private final BookingRepository repository;

    private final BookingEntityMapper mapper;

    @Override
    public void execute(StateContext<BookingState, BookingEvent> stateContext) {
        UUID bookingId = Optional.ofNullable(stateContext)
                .map(StateContext::getMessage)
                .map(Message::getHeaders)
                .map(messageHeaders -> messageHeaders.get(BOOKING_ID_HEADER, UUID.class))
                .orElseThrow(() -> new RuntimeException("Could not retrieve bookingId from stateContext"));

        BookingEntity booking = repository.getReferenceById(bookingId);
        if (repository.existsByParkingIdAndParkingSpotIdAndStartTimeAfterAndEndTimeBeforeAllIgnoreCase(booking.getParkingId(), booking.getParkingSpotId(), booking.getStartTime(), booking.getEndTime())) {
            log.error("Can not confirm booking, there exists a booking at same time: " + bookingId);
            booking.setState(BookingState.DECLINED);
        }

        BookingConfirmationMessageRequest request = mapper.bookingToConfirmationRequest(booking);

        try {
            rabbitTemplate.convertAndSend(RabbitMqConfiguration.CONFIRM_BOOKING_QUEUE, request);
            log.debug("Sent confirmation request to queue for booking id {}", request.getBookingId());
            booking.setState(BookingState.PENDING_CONFIRMATION);
        } catch (AmqpException e) {
            log.error("Failed to send BookingConfirmationRquest to queue {} for booking id {}", RabbitMqConfiguration.CONFIRM_BOOKING_QUEUE, request.getBookingId());
            throw e;
        } finally {
            repository.saveAndFlush(booking);
        }
    }
}
