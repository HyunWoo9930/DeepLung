# DeepLung Project Development Guidelines

This document provides essential information for developers working on the DeepLung project.

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

- `src/main/resources/application.properties` or `application.yml` - Main configuration file
- Environment variables can be used to override configuration for different environments

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

#### Example Test

Here's an example of a controller test using Mockito:

```java
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    
    @InjectMocks
    private UserController userController;

    private JoinRequest joinRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
        
        joinRequest = new JoinRequest();
        joinRequest.setUserId("testuser");
        joinRequest.setPassword("password123");
        joinRequest.setName("Test User");
        joinRequest.setGender(Gender.MALE);
        joinRequest.setUserType(UserType.NORMAL);
        joinRequest.setBirthYear("1990");
        joinRequest.setIsPrivateInformAgreed(true);
    }

    @Test
    void joinShouldReturnSuccess() throws Exception {
        // Given
        doNothing().when(userService).createUser(any(JoinRequest.class));

        // When & Then
        mockMvc.perform(post("/api/v1/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(joinRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("회원가입 성공"));
    }
}
```

### Test Coverage
Consider using JaCoCo for test coverage analysis:

```gradle
plugins {
    id 'jacoco'
}

test {
    finalizedBy jacocoTestReport
}

jacocoTestReport {
    dependsOn test
    reports {
        xml.required = true
        html.required = true
    }
}
```

## Additional Development Information

### Project Structure
The project follows a standard Spring Boot application structure:

- `controller`: REST API endpoints
- `service`: Business logic
- `repository`: Data access layer
- `domain`: Entity classes and DTOs
- `jwt`: JWT authentication components
- `config`: Configuration classes
- `utils`: Utility classes

### Authentication
The project uses JWT (JSON Web Token) for authentication. The key components are:

- `JwtTokenProvider`: Generates and validates JWT tokens
- `JwtAuthenticationFilter`: Intercepts requests and authenticates users based on JWT tokens

### Python Integration
The project includes Python components for lung analysis. These are located in the `python` directory and are called from the Java application.

### API Documentation
The project uses Springdoc OpenAPI for API documentation. Access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

### Coding Standards
- Follow standard Java coding conventions
- Use meaningful variable and method names
- Write comprehensive JavaDoc comments for public methods
- Keep methods small and focused on a single responsibility
- Write unit tests for all new functionality

### Troubleshooting
- Check application logs for error messages
- Verify database connection settings
- Ensure Python environment is properly configured
- For JWT issues, check token expiration and signature validation