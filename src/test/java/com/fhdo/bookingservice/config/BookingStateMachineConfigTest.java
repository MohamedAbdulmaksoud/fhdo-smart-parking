package com.fhdo.bookingservice.config;

import com.fhdo.bookingservice.BuilderUtils;
import com.fhdo.bookingservice.domain.BookingEvent;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingConfirmationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

@SpringBootTest
@Testcontainers
class BookingStateMachineConfigTest {

    @Container
    private static final RabbitMQContainer RABBIT_MQ_CONTAINER = new RabbitMQContainer("rabbitmq:3-management");

    @Autowired
    StateMachineFactory<BookingState, BookingEvent> factory;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", RABBIT_MQ_CONTAINER::getHost);
        registry.add("spring.rabbitmq.port", RABBIT_MQ_CONTAINER::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBIT_MQ_CONTAINER::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBIT_MQ_CONTAINER::getAdminPassword);
    }

    @Test
    void testBookingStateTransitionFromNewToPendingConfirmation() {
        StateMachine<BookingState, BookingEvent> sm = factory.getStateMachine();
        sm.startReactively().subscribe();

        BookingConfirmationRequest request = BuilderUtils.bookingConfirmationRequest();

        sm.startReactively().subscribe();

        Message<BookingEvent> msg = MessageBuilder.withPayload(BookingEvent.BOOK_PARKING_SLOT)
                .setHeader("booking_id", request.getBookingId())
                .setHeader(request.getHeader(), request)
                .build();

        sm.sendEvent(Mono.just(msg)).subscribe();
        Assertions.assertEquals(BookingState.PENDING_CONFIRMATION, sm.getState().getId());

    }

    @Test
    void testBookingStateTransitionFromPendingConfirmationToPendingConfirmation() {
        StateMachine<BookingState, BookingEvent> sm = factory.getStateMachine();

        BookingConfirmationRequest request = BuilderUtils.bookingConfirmationRequest();

        sm.getStateMachineAccessor()
                .doWithRegion(stateMachine -> stateMachine.resetStateMachine(new DefaultStateMachineContext<>(BookingState.PENDING_CONFIRMATION, null, null, null)));
        sm.startReactively().subscribe();

        Message<BookingEvent> msg = MessageBuilder.withPayload(BookingEvent.BOOK_PARKING_SLOT)
                .setHeader("booking_id", request.getBookingId())
                .setHeader(request.getHeader(), request)
                .build();

        sm.sendEvent(Mono.just(msg)).subscribe();
        Assertions.assertEquals(BookingState.PENDING_CONFIRMATION, sm.getState().getId());
    }

    @Test
    void testBookingStateTransitionFromPendingConfirmationToConfirmed() {
        StateMachine<BookingState, BookingEvent> sm = factory.getStateMachine();
        BookingConfirmationRequest request = BuilderUtils.bookingConfirmationRequest();

        sm.getStateMachineAccessor()
                .doWithRegion(stateMachine -> stateMachine.resetStateMachine(new DefaultStateMachineContext<>(BookingState.PENDING_CONFIRMATION, null, null, null)));
        sm.startReactively().subscribe();

        Message<BookingEvent> msg = BuilderUtils.eventMessage(request, BookingEvent.BOOKING_CONFIRMED);

        sm.sendEvent(Mono.just(msg)).subscribe();
        Assertions.assertEquals(BookingState.CONFIRMED, sm.getState().getId());
    }

    @Test
    void testBookingStateTransitionFromPendingConfirmationToDeclined() {
        StateMachine<BookingState, BookingEvent> sm = factory.getStateMachine();
        BookingConfirmationRequest request = BuilderUtils.bookingConfirmationRequest();

        sm.getStateMachineAccessor()
                .doWithRegion(stateMachine -> stateMachine.resetStateMachine(new DefaultStateMachineContext<>(BookingState.PENDING_CONFIRMATION, null, null, null)));
        sm.startReactively().subscribe();

        Message<BookingEvent> msg = BuilderUtils.eventMessage(request, BookingEvent.BOOKING_FAILED);

        sm.sendEvent(Mono.just(msg)).subscribe();
        Assertions.assertEquals(BookingState.DECLINED, sm.getState().getId());
    }


    @Test
    void testBookingStateTransitionFromNewToCancelled() {
        StateMachine<BookingState, BookingEvent> sm = factory.getStateMachine();
        BookingConfirmationRequest request = BuilderUtils.bookingConfirmationRequest();

        // Set the initial state to NEW
        sm.getStateMachineAccessor()
                .doWithRegion(stateMachine -> stateMachine.resetStateMachine(new DefaultStateMachineContext<>(BookingState.NEW, null, null, null)));
        sm.startReactively().subscribe();

        Message<BookingEvent> msg = BuilderUtils.eventMessage(request, BookingEvent.BOOKING_CANCELLED);

        sm.sendEvent(Mono.just(msg)).subscribe();
        Assertions.assertEquals(BookingState.CANCELLED, sm.getState().getId());
    }


    @Test
    void testBookingStateTransitionFromConfirmedToActive() {
        StateMachine<BookingState, BookingEvent> sm = factory.getStateMachine();
        BookingConfirmationRequest request = BuilderUtils.bookingConfirmationRequest();

        sm.getStateMachineAccessor()
                .doWithRegion(stateMachine -> stateMachine.resetStateMachine(new DefaultStateMachineContext<>(BookingState.CONFIRMED, null, null, null)));
        sm.startReactively().subscribe();

        Message<BookingEvent> msg = BuilderUtils.eventMessage(request, BookingEvent.VEHICLE_PARKED);

        sm.sendEvent(Mono.just(msg)).subscribe();
        Assertions.assertEquals(BookingState.ACTIVE, sm.getState().getId());
    }


    @Test
    void testBookingStateTransitionFromConfirmedToCancelled() {
        StateMachine<BookingState, BookingEvent> sm = factory.getStateMachine();
        BookingConfirmationRequest request = BuilderUtils.bookingConfirmationRequest();

        sm.getStateMachineAccessor()
                .doWithRegion(stateMachine -> stateMachine.resetStateMachine(new DefaultStateMachineContext<>(BookingState.CONFIRMED, null, null, null)));
        sm.startReactively().subscribe();

        Message<BookingEvent> msg = BuilderUtils.eventMessage(request, BookingEvent.BOOKING_CANCELLED);

        sm.sendEvent(Mono.just(msg)).subscribe();
        Assertions.assertEquals(BookingState.CANCELLED, sm.getState().getId());
    }


    @Test
    void testBookingStateTransitionFromActiveToOverstay() {
        StateMachine<BookingState, BookingEvent> sm = factory.getStateMachine();
        BookingConfirmationRequest request = BuilderUtils.bookingConfirmationRequest();

        sm.getStateMachineAccessor()
                .doWithRegion(stateMachine -> stateMachine.resetStateMachine(new DefaultStateMachineContext<>(BookingState.ACTIVE, null, null, null)));
        sm.startReactively().subscribe();

        Message<BookingEvent> msg = BuilderUtils.eventMessage(request, BookingEvent.OVERSTAY_OCCURRED);

        sm.sendEvent(Mono.just(msg)).subscribe();
        Assertions.assertEquals(BookingState.OVERSTAY, sm.getState().getId());
    }


    @Test
    void testBookingStateTransitionFromActiveToCompleted() {
        StateMachine<BookingState, BookingEvent> sm = factory.getStateMachine();
        BookingConfirmationRequest request = BuilderUtils.bookingConfirmationRequest();

        sm.getStateMachineAccessor()
                .doWithRegion(stateMachine -> stateMachine.resetStateMachine(new DefaultStateMachineContext<>(BookingState.ACTIVE, null, null, null)));
        sm.startReactively().subscribe();

        Message<BookingEvent> msg = BuilderUtils.eventMessage(request, BookingEvent.BOOKING_COMPLETED);

        sm.sendEvent(Mono.just(msg)).subscribe();
        Assertions.assertEquals(BookingState.COMPLETED, sm.getState().getId());
    }

}