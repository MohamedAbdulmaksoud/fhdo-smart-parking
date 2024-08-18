# Implementation and Evaluation of a Smart Parking Application as Mobility-as-a-Service Use Case

## Overview
This project focuses on the **Implementation and Evaluation of a Smart Parking Application** as a use case for Mobility-as-a-Service (MaaS). The application is designed as a microservices architecture, comprising several distinct services that work together to deliver a seamless parking experience.

## Table of Contents
- [Project Overview](#overview)
- [Features](#features)
- [Services](#services)
    - [Booking Service](#booking-service)
    - [Parking Service](#parking-service)
    - [Eureka Server](#eureka-server)
    - [Cloud Gateway](#cloud-gateway)
    - [Config Server](#config-server)
    - [MQTT Client](#mqtt-client)
- [Technologies Used](#technologies-used)
- [Architecture](#architecture)
- [Installation](#installation)
- [Usage](#usage)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)
- [Contact Information](#contact-information)

## Features
- **Smart Parking Allocation**: Dynamic allocation of parking spaces based on real-time availability.
- **User Authentication**: Secure user authentication and management.
- **Payment Integration**: Seamless payment processing for parking fees.
- **Parking Spot Reservation**: Reserve parking spots in advance.
- **Navigation Assistance**: Real-time navigation to the allocated parking spot.
- **Usage Analytics**: Comprehensive analytics for users and service providers.
- **Notification System**: Alerts for parking spot availability and expiration times.

## Services

### 1. Booking Service
- **Repository**: [Booking Service Repository](https://github.com/MohamedAbdulmaksoud/fhdo-smart-parking/tree/master/fhdo-booking-service)
- **Description**: 

### 2. Parking Service
- **Repository**: [Parking Service Repository](https://github.com/MohamedAbdulmaksoud/fhdo-smart-parking/tree/master/fhdo-parking-service)
- **Description**: 

### 3. Eureka Server
- **Repository**: [Eureka Server Repository](https://github.com/MohamedAbdulmaksoud/fhdo-smart-parking/tree/master/fhdo-eureka-server)
- **Description**: 

### 4. Cloud Gateway
- **Repository**: [Cloud Gateway Repository](https://github.com/MohamedAbdulmaksoud/fhdo-smart-parking/tree/master#:~:text=3%20hours%20ago-,fhdo%2Dcloud%2Dgateway,-Move%20fhdo%2Dcloud)
- **Description**: 

### 5. Config Server
- **Repository**: [Config Server Repository](https://github.com/MohamedAbdulmaksoud/fhdo-smart-parking/tree/master/fhdo-cloud-config-server)
- **Description**: 

### 6. MQTT Client
- **Repository**: [MQTT Client Repository](https://github.com/MohamedAbdulmaksoud/fhdo-smart-parking/tree/master#:~:text=3%20hours%20ago-,fhdo%2Dmqtt,-Move%20fhdo%2Dmqtt)
- **Description**: 

## Technologies Used
- **Frontend**: [Specify the frameworks/libraries, e.g., React, Angular, etc.]
- **Backend**: [Specify the backend technology, e.g., Spring Boot, Node.js, etc.]
- **Database**: [Specify the database, e.g., MySQL, MongoDB, etc.]
- **Service Registry**: Eureka Server
- **API Gateway**: Spring Cloud Gateway
- **Configuration Management**: Spring Cloud Config Server
- **Messaging**: MQTT
- **Other Tools**: [List any other tools used, e.g., Docker, Jenkins, etc.]

## Architecture
[Provide a brief description of the system architecture, including diagrams if available.]

## Installation
### Prerequisites
- [List any software requirements, e.g., Java, Maven, Node.js, etc.]
- [Instructions on how to install these prerequisites.]

### Installation Steps
1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/smart-parking-application.git
    ```
2. Navigate to each service's directory and follow the installation instructions provided in their respective `README.md` files.

### Starting the Services
1. **Start Config Server**:
    ```bash
    cd fhdo-cloud-config-server
    [Provide the command to start the Config Server]
    ```
2. **Start Eureka Server**:
    ```bash
    cd fhdo-eureka-server
    [Provide the command to start the Eureka Server]
    ```
3. **Start Cloud Gateway**:
    ```bash
    cd fhdo-cloud-gateway
    [Provide the command to start the Cloud Gateway]
    ```
4. **Start Parking Service**:
    ```bash
    cd fhdo-parking-service
    [Provide the command to start the Parking Service]
    ```
5. **Start Booking Service**:
    ```bash
    cd fhdo-booking-service
    [Provide the command to start the Booking Service]
    ```
6. **Start MQTT Client**:
    ```bash
    cd fhdo-mqtt
    [Provide the command to start the MQTT Client]
    ```

## Usage
1. Access the application via the Cloud Gateway.
2. Register and log in as a user.
3. Search for available parking spots using the Parking Service.
4. Book a parking spot using the Booking Service.
5. Monitor parking spot availability in real-time via the MQTT Client.

## Testing
### Running Unit Tests
```bash
[Provide commands to run unit tests, e.g., mvn test, npm test]
