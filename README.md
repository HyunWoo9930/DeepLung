# DeepLung Project

DeepLung is a comprehensive lung health analysis application that uses machine learning to predict lung risk levels based on survey data and/or X-ray images. The application provides both a standard prediction mode using only survey data and a professional mode that combines survey data with X-ray images for more accurate predictions.

## Features

- User authentication and management
- Lung risk prediction based on survey data
- Advanced multimodal prediction using both survey data and X-ray images
- Storage and retrieval of prediction history
- RESTful API for integration with frontend applications

## Technology Stack

- **Backend**: Spring Boot 3.5.0 with Java 17
- **Database**: MySQL
- **Authentication**: JWT (JSON Web Token)
- **Machine Learning**: TensorFlow with Python
- **API Documentation**: Springdoc OpenAPI (Swagger)
- **Containerization**: Docker

## Build/Configuration Instructions

### Prerequisites
- Java 17 (required by the project's toolchain configuration)
- Python 3 and pip (for the Python components)
- MySQL database

### Building the Project
The project uses Gradle as its build tool. Here are the key commands:

```bash
# Build the project
./gradlew build

# Build without running tests
./gradlew build -x test

# Clean and rebuild
./gradlew clean build
```

### Docker Deployment
The project includes a Dockerfile for containerized deployment:

```bash
# Build the JAR file
./gradlew build

# Build the Docker image
docker build -t deeplung:latest .

# Run the container
docker run -p 8080:8080 deeplung:latest
```

The Docker container includes both the Spring Boot application and the Python environment needed for the lung analysis functionality.

### Configuration
The application uses Spring Boot's standard configuration approach. Key configuration files:

- `src/main/resources/application.yml` - Main configuration file
- Environment variables can be used to override configuration for different environments

## API Endpoints

### User Management
- `POST /api/v1/user/join` - Register a new user
- `POST /api/v1/user/login` - Login and receive JWT token
- `GET /api/v1/user/info` - Get user information (requires authentication)

### Lung Analysis
- `POST /api/v1/paperweight/normal` - Run lung prediction based on survey data
- `POST /api/v1/paperweight/professional` - Run multimodal prediction using both survey data and an X-ray image
- `GET /api/v1/paperweight/` - Get a list of previous predictions
- `GET /api/v1/paperweight/{paperweight_id}` - Get detailed information about a specific prediction

### Other
- `GET /api/v1/default` - Simple health check endpoint

## Testing Information

### Running Tests
The project uses JUnit 5 for testing. Tests can be run using Gradle:

```bash
# Run all tests
./gradlew test

# Run a specific test class
./gradlew test --tests "capsthon.backend.deeplung.controller.UserControllerTest"

# Run a specific test method
./gradlew test --tests "capsthon.backend.deeplung.controller.UserControllerTest.joinShouldReturnSuccess"
```

### Writing Tests
The project follows standard Spring Boot testing practices:

1. **Unit Tests**: Test individual components in isolation using Mockito for mocking dependencies.
2. **Integration Tests**: Test the interaction between components using Spring's testing support.
3. **API Tests**: Test the REST API endpoints using MockMvc.

## Project Structure

The project follows a standard Spring Boot application structure:

- `src/main/java/capsthon/backend/deeplung/`
  - `controller/` - REST API endpoints
  - `service/` - Business logic
  - `repository/` - Data access layer
  - `domain/` - Entity classes and DTOs
  - `jwt/` - JWT authentication components
  - `config/` - Configuration classes
  - `utils/` - Utility classes

- `python/` - Python scripts and models for lung analysis
  - `lung_predict.py` - Script for prediction based on survey data
  - `multimodal_predict.py` - Script for prediction using both survey data and X-ray images
  - `*.h5` - Pre-trained TensorFlow models

## Machine Learning Models

The application uses two main prediction models:

1. **Survey-only Model** (`survey_only_lung_risk.h5`):
   - Uses 23 features including age, gender, environmental factors, and symptoms
   - Predicts lung risk level as Low, Medium, or High

2. **Multimodal Model** (`model_no_groups.h5`):
   - Combines 21 survey features with X-ray image analysis
   - Provides more accurate predictions by leveraging both data sources

## API Documentation

The project uses Springdoc OpenAPI for API documentation. Access the Swagger UI at:

```
http://localhost:8080/swagger
```

## Development Guidelines

- Follow standard Java coding conventions
- Write comprehensive JavaDoc comments for public methods
- Write unit tests for all new functionality
- Use meaningful variable and method names
- Keep methods small and focused on a single responsibility

## Troubleshooting

- Check application logs for error messages
- Verify database connection settings
- Ensure Python environment is properly configured
- For JWT issues, check token expiration and signature validation
