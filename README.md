# S4.02 — Fruit API (H2) 🍎

## 📄 Description

This project is a **RESTful API developed with Spring Boot** to manage the stock of fruits in a store.  
It provides full **CRUD functionality (Create, Read, Update, Delete)** and persists data using an **H2 in-memory database**, ideal for development and testing environments.

The application is structured following the **MVC pattern**, uses **DTOs to isolate the persistence layer**, applies **Bean Validation** to incoming requests, and includes **global exception handling** to ensure consistent and meaningful error responses.

The project has been developed following **TDD principles**, including both **unit tests** and **controller integration tests**, and is packaged using a **multi-stage Docker build** ready for production environments.

---

## 💻 Technologies Used

- **Java 21 (LTS)**
- **Spring Boot 3.x**
  - Spring Web
  - Spring Data JPA
  - Bean Validation (Jakarta Validation)
- **H2 Database**
- **Maven**
- **JUnit 5**
- **Mockito**
- **MockMvc**
- **Docker (multi-stage build)**

---

## 📋 Requirements

| Tool | Version |
|------|----------|
| Java | 21 |
| Maven | 3.9+ |
| Docker | 24+ (optional) |

---

## 🛠️ Installation & Setup

### 1️⃣ Clone the repository
```bash
https://github.com/christo256/S4.02-Api-Rest-With-Spring-Boot
cd fruit-api-h2
```

### 2️⃣ Build the project
```bash
mvn clean package
```

### 3️⃣ Run the application
```bash
java -jar target/fruit-api-h2-0.0.1-SNAPSHOT.jar
```
The application will start on port 8080.

---

## 🌐 Available Endpoints
| Method     | Endpoint       | Description              | Response                                     |
| ---------- | -------------- | ------------------------ | -------------------------------------------- |
| **POST**   | `/fruits`      | Create a new fruit       | `201 Created`                                |
| **GET**    | `/fruits`      | Retrieve all fruits      | `200 OK`                                     |
| **GET**    | `/fruits/{id}` | Retrieve a fruit by ID   | `200 OK` / `404 Not Found`                   |
| **PUT**    | `/fruits/{id}` | Update an existing fruit | `200 OK`, `400 Bad Request`, `404 Not Found` |
| **DELETE** | `/fruits/{id}` | Delete a fruit by ID     | `204 No Content`                             |

---

## 🧪 Testing
### 🧩 Unit Tests (Service Layer)

Implemented using **JUnit 5** and **Mockito**

The repository is mocked to isolate the business logic

Tests cover:

- Fruit creation

- Retrieval by ID

- Retrieval of all fruits

- Deletion

- Exception handling when entities are not found

### 🔍 Integration Tests (Controller Layer)

Implemented using **MockMvc**

Simulates real HTTP requests without starting a server

Verifies:

- HTTP status codes

- JSON request and response bodies

- Validation errors

- Global exception handling behavior

### ✅ Test Strategy

Tests follow the **RED** → **GREEN** → **REFACTOR** cycle

All controller endpoints and service methods are covered

Both happy paths and error paths are tested

---

## 🧾 Global Exception Handling

All application exceptions are handled centrally using a @ControllerAdvice-based GlobalExceptionHandler.

This ensures:

- Consistent error responses

- Clear HTTP status codes

- Readable error messages for clients

Example error response:
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Fruit with id 99 not found"
}
```


Handled exceptions include:

- `FruitNotFoundException`

- `MethodArgumentNotValidException`

- Validation-related errors triggered by `@Valid`

---

## 🧩 DTOs and Validation

DTOs are used to avoid exposing JPA entities directly through the API.

Example request DTO:
```java
public class FruitRequestDTO {

    @NotBlank(message = "Fruit name cannot be blank")
    private String name;

    @Positive(message = "Weight must be greater than zero")
    private int weightInKilos;
}
```

This approach:

- Enforces data integrity

- Keeps the persistence layer decoupled from the API

- Produces clear validation error messages

---

## 🐳 Docker Build (Multi-stage)

This project includes a multi-stage Dockerfile optimized for production usage.
```dockerfile
# ===============================
# Stage 1 — Build
# ===============================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline

COPY src src
RUN ./mvnw clean package -DskipTests

# ===============================
# Stage 2 — Runtime
# ===============================
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 📘 Docker Build Explanation
| Stage       | Purpose                                           | Base Image               |
| ----------- | ------------------------------------------------- | ------------------------ |
| **Build**   | Compiles the application and generates the JAR    | `eclipse-temurin:21-jdk` |
| **Runtime** | Runs only the compiled JAR in a lightweight image | `eclipse-temurin:21-jre` |


Using a multi-stage build reduces the final image size and ensures that only the necessary runtime artifacts are included.

---

## 💡 Docker Commands

Build the Docker image:
```bash
docker build -t fruit-api-h2 .
```

Run the container:
```bash
docker run -p 8080:8080 fruit-api-h2
```

Access the API at:
👉 http://localhost:8080/fruits
