# User Service

The User Service is a microservice that manages user data and authentication for the Smart Parking system. It includes functionality for user registration, login, and validation.

## Prerequisites

- Java 17
- Maven 3.8.4 or higher
- PostgreSQL (or compatible database)

## Building the Application

1. **Clone the Repository**

   ```bash
   git clone https://github.com/your-repository/user-service.git
   cd user-service
   ```

2. **Build the Project**

   ```bash
   mvn clean package
   ```

   This command compiles the code, runs tests, and packages the application into a JAR file.

## Running the Application

1. **Set Up the Database**

   Ensure that a PostgreSQL database is available, and update the `application.properties` or `application.yml` file with the correct database credentials.

2. **Start the Application**

   ```bash
   mvn spring-boot:run
   ```

   Alternatively, you can run the JAR file directly:

   ```bash
   java -jar target/user-service-0.0.1-SNAPSHOT.jar
   ```

## Features

- User registration with validation
- Secure user authentication
- Role-based access control
- Integration with other services for user data retrieval

## REST API

### Endpoints

#### Register User

- **URL**: `/api/v1/users/register`
- **Method**: `POST`
- **Request Body**: `UserDto`
- **Response**: JSON containing the user's ID and details

#### Login User

- **URL**: `/api/v1/users/login`
- **Method**: `POST`
- **Request Body**: JSON with `email` and `password`
- **Response**: Authentication token and user details

#### Get User Info

- **URL**: `/api/v1/users/me`
- **Method**: `GET`
- **Headers**: `Authorization: Bearer <token>`
- **Response**: JSON containing the user's details

## Components

### Security Configuration

- **`SecurityConfig`**: Configures authentication, authorization, and security settings for the service.

### Repository

- **`UserRepository`**: Provides data access for user entities, including methods to find users by email.

### Services

- **`UserService`**: Contains core business logic for managing user operations such as registration and authentication.

## Validation

User inputs are validated using Java Bean Validation. Key rules include:

- **Email**: Must be a valid email format.
- **Name**: Cannot be blank and must be between 2 and 50 characters.
- **Password**: Cannot be blank and must be at least 8 characters long.