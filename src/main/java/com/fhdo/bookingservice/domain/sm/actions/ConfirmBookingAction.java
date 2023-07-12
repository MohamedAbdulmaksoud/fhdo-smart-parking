package com.fhdo.bookingservice.domain.sm.actions;

import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingConfirmationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConfirmBookingAction implements Action<BookingState, BookingEvent> {

    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BookingState, BookingEvent> stateContext) {
        BookingConfirmationRequest request = Optional.ofNullable(stateContext)
                .map(StateContext::getMessage)
                .map(Message::getHeaders)
                .map(messageHeaders -> messageHeaders.get(BookingConfirmationRequest.HEADER_NAME, BookingConfirmationRequest.class))
                .orElseThrow(() -> new RuntimeException("Could not extract confirmation request from headers"));

        // TODO: 07.07.23: Configure JMS and catch JMS exception
        //  jmsTemplate.convertAndSend(JmsConfig.CONFIRM_ORDER_QUEUE, request);

        log.debug("Sent confirmation request to queue for booking id {}", request.getBookingId());
    }
}
