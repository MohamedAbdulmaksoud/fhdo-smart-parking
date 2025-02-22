# FHDO UI

The FHDO UI is a web-based user interface for the Smart Parking system. It enables users to interact with the system for operations such as viewing available parking spots, registering, logging in, and managing user profiles.

## Prerequisites

- Java 17
- Maven 3.8.4 or higher
- Node.js and npm (for front-end assets)

## Building the Application

1. **Clone the Repository**

   ```bash
   git clone https://github.com/your-repository/fhdo-ui.git
   cd fhdo-ui
   ```

2. **Install Front-End Dependencies**

   ```bash
   npm install
   ```

3. **Build the Project**

   ```bash
   mvn clean package
   ```

   This command compiles the back-end and packages the front-end assets.

## Running the Application

1. **Run the Back-End**

   ```bash
   mvn spring-boot:run
   ```

   Alternatively, you can run the packaged JAR:

   ```bash
   java -jar target/fhdo-ui-0.0.1-SNAPSHOT.jar
   ```

2. **Access the UI**

   Once the application is running, navigate to `http://localhost:8080` in your web browser.

## Features

- User registration and login
- Viewing nearby parking spots
- Filtering parking spots by type and availability
- Notifications for important events
- Responsive design for mobile and desktop

## Components

### UI Components

- **RegistrationView**: Handles user registration.
- **LoginView**: Manages user authentication.
- **MainLayout**: The main container for the application.
- **NotificationUtil**: Utility for displaying notifications.

### DTOs

- **NearbyParkingRequest**: Data structure for requesting nearby parking spots.
- **NearbyParkingResponse**: Response structure for nearby parking spots.
- **UserDto**: Data structure for user registration and login.

### Configuration

- **AppConfig**: General application configuration.
- **DevConfig**: Development-specific settings, such as loading sample parking data.

## Testing

Unit and integration tests are included for critical components. Run tests using:

```bash
mvn test
```
