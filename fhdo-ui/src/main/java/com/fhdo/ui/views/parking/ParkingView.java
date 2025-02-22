package com.fhdo.ui.views.parking;

import com.fhdo.ui.dto.*;
import com.fhdo.ui.service.AddressAutocompleteService;
import com.fhdo.ui.service.ParkingLotService;
import com.fhdo.ui.service.ParkingSpaceService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "parking", layout = com.fhdo.ui.layout.MainLayout.class)
public class ParkingView extends VerticalLayout {

    private final ParkingLotService parkingLotService;
    private final ParkingSpaceService parkingSpaceService;
    private final AddressAutocompleteService addressAutocompleteService;

    private final Grid<NearbyParkingResponse> parkingGrid = new Grid<>(NearbyParkingResponse.class);
    private final Grid<ParkingSpotCount> spotGrid = new Grid<>(ParkingSpotCount.class);

    @Autowired
    public ParkingView(ParkingLotService parkingLotService, ParkingSpaceService parkingSpaceService, AddressAutocompleteService addressAutocompleteService) {
        this.parkingLotService = parkingLotService;
        this.parkingSpaceService = parkingSpaceService;
        this.addressAutocompleteService = addressAutocompleteService;

        setAlignItems(Alignment.CENTER);

        // Address Autocomplete ComboBox
        ComboBox<String> locationComboBox = new ComboBox<>("Enter Address or Location");
        locationComboBox.setPlaceholder("Start typing an address...");
        locationComboBox.setAllowCustomValue(false);

        // Lazy-loading data for ComboBox
        locationComboBox.setItems(query -> {
            String filter = query.getFilter().orElse("");
            return addressAutocompleteService.getSuggestions(filter).stream();
        });

        locationComboBox.addValueChangeListener(event -> {
            String selectedAddress = event.getValue();
            if (selectedAddress != null && !selectedAddress.isEmpty()) {
                Notification.show("Selected Address: " + selectedAddress, 3000, Notification.Position.TOP_CENTER);
            }
        });

        // Radius Field
        TextField radiusField = new TextField("Search Radius (meters)");
        radiusField.setPlaceholder("E.g., 1000");

        // Sorting Preference ComboBox
        ComboBox<String> sortingPreference = new ComboBox<>("Sorting Preference");
        sortingPreference.setItems("LEAST_WALKING_DISTANCE", "LEAST_TRIP_TIME");
        sortingPreference.setValue("LEAST_WALKING_DISTANCE");

        // Search Button
        Button searchButton = new Button("Search Nearby Parking", e -> {
            String address = locationComboBox.getValue();
            String radius = radiusField.getValue();
            String sorting = sortingPreference.getValue();
            searchNearbyParking(address, radius, sorting);
        });

        // Parking Grid
        parkingGrid.setColumns("placeId", "fullName", "distance", "isOpen");
        parkingGrid.addItemClickListener(event -> viewParkingSpaces(event.getItem().getPlaceId()));

        // Spot Grid
        spotGrid.setColumns("type", "availableSpots");

        // Add all components to the layout
        add(locationComboBox, radiusField, sortingPreference, searchButton, parkingGrid, spotGrid);
    }

    private void searchNearbyParking(String address, String radius, String sorting) {
        try {
            if (address == null || address.isEmpty()) {
                Notification.show("Please select a valid address.", 3000, Notification.Position.TOP_CENTER);
                return;
            }

            NearbyParkingRequest request = new NearbyParkingRequest();
            request.setAddress(address);
            request.setDistanceInMeters(Double.parseDouble(radius));
            request.setPreference(SortingPreference.valueOf(sorting));

            List<NearbyParkingResponse> parkingList = parkingLotService.findNearbyParking(request);
            parkingGrid.setItems(parkingList);

            Notification.show("Search completed successfully.", 3000, Notification.Position.TOP_CENTER);
        } catch (NumberFormatException ex) {
            Notification.show("Invalid radius. Please enter a valid number.", 3000, Notification.Position.TOP_CENTER);
        } catch (Exception ex) {
            Notification.show("Failed to search parking lots: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void viewParkingSpaces(String parkingId) {
        try {
            AvailableParkingSpacesResponse response = parkingSpaceService.getAvailableSpaces(parkingId);
            spotGrid.setItems(response.getAvailableSpots());

            Notification.show("Parking space details loaded.", 3000, Notification.Position.TOP_CENTER);
        } catch (Exception ex) {
            Notification.show("Failed to load parking spaces: " + ex.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }
}