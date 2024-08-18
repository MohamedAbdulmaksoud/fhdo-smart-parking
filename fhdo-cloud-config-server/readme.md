# FHDO Cloud Config Server

## Overview

The `fhdo-cloud-config-server` is a Spring Boot application that provides centralized configuration management for
the `fhdo-smart-parking` project. It uses Spring Cloud Config
Server https://docs.spring.io/spring-cloud-config/docs/current/reference/html/
to serve configuration properties from a Git repository.

## Prerequisites

- Java 17
- Maven 3.8.4 or higher

## Building the Application

1. **Clone the Repository**

   ```bash
   git clone https://github.com/your-repository/config-server.git
   cd config-server
   ```
2. **Build the Project**
   ```bash
   mvn clean package
   ```
   This command will compile the code, run tests, and package the application into a JAR file.

### Running the Application

Once the services are up and running, you can start the config-server application.

   ```bash
   mvn spring-boot:run
   ```

Alternatively, you can run the JAR file directly:

   ```bash
   java -jar target/config-server-0.0.1-SNAPSHOT.jar
   ```

The application will start on port 8888 by default, as specified in the application.yml file.

## Configuration
The config-server is configured to use a Git repository as its backend for configuration properties. The repository URL is specified in the application.yml file.
- **`Port`**: The server runs on port `8888`.
- **`Git Repository`**: Configuration properties are fetched from https://github.com/MohamedAbdulmaksoud/cloud-config-repo.


**The HTTP service has resources in the following form:**
<ul>
<li>/{application}/{profile}[/{label}</li>
<li>/{application}-{profile}.yml</li>
<li>/{label}/{application}-{profile}.yml</li>
<li>/{application}-{profile}.properties</li>
<li>/{label}/{application}-{profile}.properties</li>
</ul>





