package com.fhdo.ui.config;

import com.fhdo.ui.dto.ParkingLotEntity;
import com.fhdo.ui.dto.ParkingSpaceEntity;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Configuration
@Profile("dev")
public class DevConfig implements VaadinServiceInitListener {

    @Value("${dev.user.email}")
    private String devUserEmail;

    @Value("${dev.csv.parkingLots}")
    private String parkingLotsCsv;

    @Value("${dev.csv.parkingSpots}")
    private String parkingSpotsCsv;

    private final List<ParkingLotEntity> parkingLots = new ArrayList<>();
    private final List<ParkingSpaceEntity> parkingSpaces = new ArrayList<>();

    @PostConstruct
    public void setupDevelopmentEnvironment() {
        loadParkingData();
    }

    private void autoLogin() {
        VaadinSession.getCurrent().setAttribute("userEmail", devUserEmail);
        VaadinSession.getCurrent().setAttribute("userName", devUserEmail);
        System.out.println("Automatically logged in as " + devUserEmail);
    }

    private void loadParkingData() {
        try {
            // Load parking lots
            try (var reader = new InputStreamReader(getClass().getResourceAsStream(parkingLotsCsv));
                 var parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
                for (CSVRecord record : parser) {
                    ParkingLotEntity lot = new ParkingLotEntity();
                    lot.setPlaceId(record.get("PlaceId"));
                    lot.setFullName(record.get("FullName"));
                    lot.setTotalCapacity(Integer.parseInt(record.get("TotalCapacity")));
                    // Add other fields as needed
                    parkingLots.add(lot);
                }
            }

            // Load parking spaces
            try (var reader = new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(parkingSpotsCsv)));
                 var parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
                for (CSVRecord record : parser) {
                    ParkingSpaceEntity space = new ParkingSpaceEntity();
                    space.setParkingId(java.util.UUID.fromString(record.get("ParkingId")));
                    space.setInternalId(Integer.parseInt(record.get("InternalId")));
                    space.setIsReserved(Boolean.parseBoolean(record.get("IsReserved")));
                    space.setIsOccupied(Boolean.parseBoolean(record.get("IsOccupied")));
                    // Add other fields as needed
                    parkingSpaces.add(space);
                }
            }
            System.out.println("Parking data loaded successfully.");
        } catch (Exception ex) {
            System.out.println("Failed to load parking data: " + ex.getMessage());
        }
    }

    public List<ParkingLotEntity> getParkingLots() {
        return parkingLots;
    }

    public List<ParkingSpaceEntity> getParkingSpaces() {
        return parkingSpaces;
    }

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        serviceInitEvent.getSource().addSessionInitListener(sessionInitEvent -> {
            autoLogin();
        });
    }

}
