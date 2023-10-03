package com.fhdo.bookingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fhdo.bookingservice.BuilderUtils;
import com.fhdo.bookingservice.domain.BookingState;
import com.fhdo.bookingservice.domain.request.BookingCreationRequest;
import com.fhdo.bookingservice.domain.response.BookingBaseResponse;
import com.fhdo.bookingservice.domain.response.BookingFullResponse;
import com.fhdo.bookingservice.services.BookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookingService bookingService;
    @Test
    @Transactional
    public void testCreateBookingIntegration() throws Exception {
        BookingCreationRequest request = BuilderUtils.bookingCreationRequest();

        String requestJson = objectMapper.writeValueAsString(request);

        String rawResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bookings/create")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();


        BookingBaseResponse response = objectMapper.readValue(rawResponse, BookingBaseResponse.class);
        Assertions.assertNotNull(response.getBookingId());
        Assertions.assertEquals(BookingState.NEW, response.getState());
        Assertions.assertEquals("2d7bb435-ce39-4bbd-9fd8-44377a4680dd", response.getUserId().toString());
    }

    @Test
    @Transactional
    public void testGetBookingIntegration() throws Exception {
        BookingCreationRequest request = BuilderUtils.bookingCreationRequest();
        UUID bookingId = bookingService.newBooking(request).getBookingId();

        // Perform the GET request
        String rawResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/bookings/{bookingId}", bookingId)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();


        BookingFullResponse response = objectMapper.readValue(rawResult, BookingFullResponse.class);

        Assertions.assertEquals(bookingId, response.getBookingId());
        Assertions.assertEquals("2d7bb435-ce39-4bbd-9fd8-44377a4680dd", response.getUserId().toString());
        Assertions.assertEquals("b2ba6718-68d4-4524-aa14-5416eff38664", response.getParkingId().toString());
        Assertions.assertEquals(1, response.getParkingSpotId());
        Assertions.assertEquals(BigDecimal.ONE, response.getBaseCost());
        Assertions.assertEquals(BookingState.NEW, response.getState());
    }

    @Test
    @Transactional
    public void testDeleteBookingIntegration() throws Exception {
        BookingCreationRequest request = BuilderUtils.bookingCreationRequest();
        UUID bookingId = bookingService.newBooking(request).getBookingId();

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/bookings/{bookingId}", bookingId));

        result.andExpect(status().isOk());
    }
}