# FHDO Cloud Gateway

## Overview

The `fhdo-cloud-gateway` is a Spring Boot application that acts as a gateway for routing requests to various
microservices in the `fhdo-smart-parking` project. It uses Spring Cloud Gateway for dynamic routing and service
discovery.

## Prerequisites

- Java 17
- Maven 3.8.4 or higher

## Building the Application

1. **Clone the Repository**

   ```bash
   git clone https://github.com/your-repository/cloud-gateway.git
   cd cloud-gateway
   ```
2. **Build the Project**
   ```bash
   mvn clean package
   ```
   This command will compile the code, run tests, and package the application into a JAR file.

### Running the Application

Once the services are up and running, you can start the booking-service application.

   ```bash
   mvn spring-boot:run
   ```

Alternatively, you can run the JAR file directly:

   ```bash
  java -jar target/cloud-gateway-0.0.1-SNAPSHOT.jar
   ```

The application will start on port 8080 by default, as specified in the application.yml file.

## Configuration

The cloud-gateway routes incoming requests to different microservices based on the profile configuration.

### Profiles

- **`Local Discovery`**: Routes requests using service discovery with Eureka.
- **`Default`**:  Routes requests to specific ports on localhost.

### Route Configuration

#### Routes Configuration for Local Discovery (`local-discovery` profile)

| **Path Pattern**                 | **Service URI**                     |
|----------------------------------|-------------------------------------|
| `/api/v1/booking/*`              | `lb://booking-service`              |
| `/api/v1/vehicle-registration/*` | `lb://vehicle-registration-service` |
| `/api/v1/parking-lots/*`         | `lb://parking-service`              |

#### Routes Configuration for Local Discovery (`default` profile)

| **Path Pattern**                 | **Service URI**         |
|----------------------------------|-------------------------|
| `/api/v1/booking/*`              | `http://localhost:8081` |
| `/api/v1/vehicle-registration/*` | `http://localhost:8082` |
| `/api/v1/parking-lots/*`         | `http://localhost:8083` |
