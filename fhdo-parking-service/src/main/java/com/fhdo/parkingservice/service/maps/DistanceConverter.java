package com.fhdo.parkingservice.service.maps;

public class DistanceConverter {

    public static long convertToMeters(String humanReadable) {
        if (humanReadable == null || humanReadable.isEmpty()) {
            throw new IllegalArgumentException("Invalid distance input");
        }

        String[] parts = humanReadable.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid distance format");
        }

        double value;
        try {
            value = Double.parseDouble(parts[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value in distance");
        }

        String unit = parts[1].toLowerCase();
        return switch (unit) {
            case "m" -> (long) value;
            case "km" -> (long) (value * 1000);
            default -> throw new IllegalArgumentException("Unsupported distance unit: " + unit);
        };
    }
}
