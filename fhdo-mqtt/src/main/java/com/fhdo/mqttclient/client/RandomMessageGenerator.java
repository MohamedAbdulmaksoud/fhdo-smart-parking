package com.fhdo.mqttclient.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Profile("simulation")
public class RandomMessageGenerator implements MessageGenerator {

    private static final int PARKING_SPACES_COUNT = 150;

    private static final List<String> MUNICIPALITIES = createMunicipalitiesList();

    private final AtomicInteger parkingIndex;

    private final AtomicInteger parkingSpotIndex;

    RandomMessageGenerator() {
        parkingIndex = new AtomicInteger(0);
        parkingSpotIndex = new AtomicInteger(0);
    }

    @Override
    public ParkingSensorMessage generateMessage() {
        String municipality = getMunicipalityName(parkingIndex.get());
        ParkingSensorMessage sensorMessage = new ParkingSensorMessage(municipality, parkingIndex.get(), parkingSpotIndex.get(), getRandomBoolean());
        incrementParkingIndices();
        return sensorMessage;
    }

    private String getMunicipalityName(int index) {
        if (index >= 0 && index < MUNICIPALITIES.size()) {
            return MUNICIPALITIES.get(index);
        }
        throw new IndexOutOfBoundsException("Invalid index: " + index);
    }

    private static List<String> createMunicipalitiesList() {
        return List.of("Innenstadt-Nord", "Innenstadt-Ost", "Innenstadt-West", "Lütgendortmund", "Eving", "Scharnhorst", "Brackel", "Aplerbeck", "Hörde", "Hombruch", "Lütgendortmund", "Mengede");
    }

    private boolean getRandomBoolean() {
        return Math.random() < 0.8;
    }

    private void incrementParkingIndices() {
        if (parkingIndex.get() == MUNICIPALITIES.size() - 1) {
            parkingIndex.set(0);
        } else {
            parkingIndex.incrementAndGet();
        }
        if (parkingSpotIndex.get() == PARKING_SPACES_COUNT) {
            parkingSpotIndex.set(1);
        } else {
            parkingSpotIndex.incrementAndGet();
        }
    }

}
