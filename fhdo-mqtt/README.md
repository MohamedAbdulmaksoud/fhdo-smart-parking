# MQTT Client

The `mqtt-client` service is a Spring Boot application that connects to an MQTT broker to publish simulated parking sensor messages at regular intervals. It leverages MQTT version 5 for messaging and uses Spring Cloud Stream for integration.

## Overview

The MQTT Client is designed to simulate and publish parking sensor data to an MQTT broker. The service is configured to connect to a broker running locally and send periodic messages with simulated parking data.

## Features

- Connects to an MQTT broker using MQTT version 5.
- Publishes simulated parking sensor messages.
- Configurable MQTT client settings and message formats.
- Uses Jackson for JSON serialization and deserialization.

## Configuration

### MQTT Client Configuration

- **Broker URI**: `tcp://localhost:1883`
- **Client ID**: `ParkingSimulator`

### Application Profiles

- **simulation**: Activates the `RandomMessageGenerator` to generate and publish simulated messages.

## Dependencies

- **Spring Cloud Stream**: For messaging support.
- **Eclipse Paho MQTT v5 Client**: For MQTT communication.
- **Micrometer Prometheus**: For metrics collection.
- **Jackson Datatype JS310**: For Java 8 date/time serialization.

## Running the Application

1. **Start the MQTT Broker**: Ensure an MQTT broker (e.g., Mosquitto) is running on `localhost` port `1883`.
2. **Build the Project**: Use Maven to build the project:
    ```bash
    mvn clean install
    ```
3. **Run the Application**: Start the Spring Boot application:
    ```bash
    mvn spring-boot:run
    ```

## Command Line Runner

The application uses `CommandLineRunner` to start publishing messages at a fixed rate (every second) once the application starts.

## Application Properties

- **Server Port**: Configured to run on port `8080`.
- **MQTT Configuration**: Includes connection details and initial message setup.