# Currency Converter Project
## Overview
The Currency Converter project is a Java-based application that allows users to convert currency rates and retrieve information about different currencies. The project includes features such as updating currency rates from an external API, converting currency, and retrieving a list of currencies. It is built using Spring Boot, Hibernate, and PostgreSQL.

## Project Structure
The project is structured into different packages, each serving a specific purpose:

### controller: 
Contains controllers responsible for handling incoming HTTP requests.

### domain:
Defines the domain entities used in the project, such as CurrencyEntity.
Contains Data Transfer Objects (DTOs) used for communication between different layers.

### mapper: 
Includes mappers responsible for mapping between different types, such as mapping DTOs to entities.

### repository: 
Houses the JPA repository interfaces for database interactions.

### service: 
Implements the business logic and services for the currency conversion functionalities.

### validation: 
Contains custom validation classes.

### aspect: 
Includes classes related to Aspect-Oriented Programming (AOP), providing cross-cutting concerns such as logging.

### client: 
Contains the currency client for fetching data from an external API.

### resources: 
Includes the application configuration file (application.yaml) and Flyway migration script (currency_data.sql).

### test: 
Contains unit and integration tests for different components.

## Getting Started
Prerequisites
Before running the project, ensure that you have the following prerequisites installed:

Java Development Kit (JDK)
Docker

## Running the Project

### Clone the repository to your local machine:
git clone <[repository-url](https://github.com/KharitonovPS/currency-convert)>

### Navigate to the project directory:
cd currency-converter

### Run the Docker Compose to start the PostgreSQL database:
docker-compose up -d

### Run the Spring Boot application using the Gradle wrapper:
./gradlew clean build
./gradlew bootRun

The application should now be running, and you can access it at http://localhost:8080.

### Running Tests
To run the tests, execute the following command:
./gradlew test

## Configuration
The main application configuration is located in the src/main/resources/application.yaml file.
Customize the configurations, such as the database connection and external API properties, as needed.

## External API Client
client package contains the currency client (CurrencyClientImpl) responsible for fetching data from an external API (https://openexchangerates.org).

## Database Migration
The database migration is managed using Flyway. The migration script (currency_data.sql) is located in the src/main/resources directory.

Feel free to contribute to the project and make it even better! If you encounter any issues or have suggestions, please create an issue on the repository.

## Thank you for using the Currency Converter project!
