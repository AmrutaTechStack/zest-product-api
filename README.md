# 🚀 Zest Product API

A secure, enterprise-grade Spring Boot REST API demonstrating clean layered architecture, JWT authentication, validation, global exception handling, Swagger documentation, Docker deployment, and proper test structure.

---

# 📌 Project Objective

This project demonstrates:

- 🔐 JWT Authentication & Authorization
- 🏗 Clean Layered Architecture
- 📦 DTO Pattern Implementation
- 📘 Swagger API Documentation
- 🐳 Dockerized Deployment
- ⚠ Global Exception Handling
- 🧪 Unit & Controller Testing
- 🧩 Modular Component Design

---

# 🏗️ Complete Application Flow (Detailed Component Chain)

```
Client (Postman / Swagger UI)
        │
        ▼
Spring Boot DispatcherServlet
        │
        ▼
Security Filter Chain
        │
        ├── JwtAuthenticationFilter
        │       │
        │       ├── Extract Bearer Token
        │       ├── Validate Token (JwtTokenProvider)
        │       ├── Load User (CustomUserDetailsService)
        │       └── Set Authentication in SecurityContext
        │
        ▼
Controller Layer
        │
        ▼
Service Layer
        │
        ▼
Repository Layer (Spring Data JPA)
        │
        ▼
Database (MySQL / H2)
        │
        ▼
Response returned to Client
```

---

# 🔐 Authentication Flow (Login)

```
Login Request
     │
     ▼
AuthenticationManager
     │
     ▼
CustomUserDetailsService
     │
     ▼
PasswordEncoder Validation
     │
     ▼
JWT Token Generation
     │
     ▼
Return JWT to Client
```

---

# 📂 Complete Package Structure

```
com.zest.productapi
│
├── controller
│   └── ProductController
│
├── service
│   ├── ProductService
│   └── ProductServiceImpl
│
├── repository
│   └── ProductRepository
│
├── entity
│   ├── Product
│   ├── User
│   └── RefreshToken
│
├── dto
│   ├── ProductRequestDTO
│   └── ProductResponseDTO
│
├── security
│   ├── SecurityConfig
│   ├── JwtAuthenticationFilter
│   ├── JwtTokenProvider
│   └── CustomUserDetailsService
│
├── config
│   └── SwaggerConfig (if custom configuration added)
│
├── exception
│   ├── GlobalExceptionHandler
│   └── ResourceNotFoundException
│
└── ProductApiApplication
```

---

# 🧪 Test Package Structure

```
src/test/java/com/zest/productapi
│
├── controller
│   └── ProductControllerTest
│
├── service
│   └── ProductServiceTest
│
├── repository
│   └── ProductRepositoryTest
│
└── security
    └── JwtAuthenticationTest
```

---

# 🧪 Test Execution Flow

```
JUnit
   │
   ▼
SpringBootTest / WebMvcTest
   │
   ▼
MockMvc
   │
   ▼
Controller Layer
   │
   ▼
Mocked Service / Repository
```

---

# 🗄️ Entity Relationship Diagram

```
User
-----
id (PK)
username
password
role

RefreshToken
------------
id (PK)
token
expiryDate
user_id (FK → User.id)

Product
-------
id (PK)
productName
description
price
createdBy
createdDate
```

### Relationship

```
User (1) -------- (1) RefreshToken
```

---

# 📘 Swagger Documentation

### Why Swagger Dependency Added?

Swagger (OpenAPI) is included to:

- 📖 Generate live API documentation
- 🧪 Allow API testing via browser
- 📋 Define request & response models
- 👨‍💻 Improve frontend/backend integration
- 🎯 Enhance assignment presentation quality

### Swagger URL

```
http://localhost:8080/swagger-ui.html
```

---

# 🐳 Docker Deployment

## 📄 Dockerfile

```dockerfile
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/zest-product-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 📄 docker-compose.yml

```yaml
version: '3.8'

services:
  app:
    build: .
    container_name: zest-product-api
    ports:
      - "8080:8080"
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/zestdb
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root

  db:
    image: mysql:8
    container_name: zest-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: zestdb
    ports:
      - "3306:3306"
```

---

# ▶ How To Run

## 🔹 Using Maven

```
mvn clean install
mvn spring-boot:run
```

---

## 🔹 Using Docker

```
mvn clean package
docker-compose up --build
```

---

# 📌 API Endpoints

| Method | Endpoint | Description |
|--------|----------|------------|
| POST | /api/v1/products | Create product |
| GET | /api/v1/products | Get all products |
| GET | /api/v1/products/{id} | Get product by ID |
| PUT | /api/v1/products/{id} | Update product |
| DELETE | /api/v1/products/{id} | Delete product |

---

# 📷 Screenshots

Store screenshots inside:

```
Zest-product-api/screenshots
```

---

## 📸 Swagger UI

```
src/main/resources/static/images/swagger.png
```

```markdown
![Swagger UI](static/images/swagger.png)
```

---

## 📸 CRUD Output

```
src/main/resources/static/images/crud-output.png
```

```markdown
![CRUD Output](static/images/crud-output.png)
```

---

# ⚠ Global Exception Handling

Centralized via:

```
GlobalExceptionHandler
```

Handles:

- 400 – Validation Errors
- 401 – Unauthorized
- 404 – Resource Not Found
- 500 – Internal Server Error

Example Response:

```json
{
  "timestamp": "2026-02-20T10:15:30",
  "status": 404,
  "message": "Product not found"
}
```

---

# 🛠 Technology Stack

- ☕ Java 17
- 🌱 Spring Boot 3
- 🔐 Spring Security + JWT
- 🗄 Spring Data JPA
- 🐬 MySQL / H2
- 📘 Swagger OpenAPI
- 🐳 Docker & Docker Compose
- 🧪 JUnit 5
- 📦 Maven

---

# 🎯 Production-Ready Features

✔ Stateless JWT authentication  
✔ Secure password encryption  
✔ DTO-based API responses  
✔ Clean separation of concerns  
✔ Centralized exception handling  
✔ Docker container support  
✔ Test coverage for major layers

---

# 🏁 Conclusion

Zest Product API demonstrates enterprise-level backend development standards including security, modular design, testing strategy, documentation, and containerized deployment. This implementation is scalable, maintainable, and production-ready.
