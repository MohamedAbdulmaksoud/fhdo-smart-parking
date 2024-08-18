# Eureka Server

The `eureka-server` service is a Spring Cloud Netflix Eureka server used for service registration and discovery within
the `fhdo-smart-parking` project.

## Overview

The Eureka Server is responsible for maintaining a registry of all services registered with it. It allows services to
discover each other and communicate within the ecosystem.

## Configuration

### `application.properties`

```properties
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
logging.level.com.netflix.eureka=OFF
logging.level.com.netflix.discovery=OFF
```

## Building and Running

1. **Using Maven**

   To build the project:
   ```bash 
   mvn clean package
   ```

   To run the application:

   ```bash
   mvn spring-boot:run
   ```

2. **Docker**
   To build the Docker image:

   ```bash 
   docker build -t eureka-server .
   ```

   To run the Docker container:

   ```bash
   docker run -p 8761:8761 eureka-server
   ```
   
## Service Discovery
Once the Eureka Server is running, other services can register with it and discover other registered services. Ensure that your service configurations are set to register with this Eureka Server.
For further information, refer to the [Spring Cloud Eureka documentation](https://spring.io/projects/spring-cloud-netflix).
