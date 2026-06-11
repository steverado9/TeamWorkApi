# TeamWork API

A RESTful API built with **Spring Boot** that enables employees within an organization to collaborate by sharing articles and GIF posts, commenting on content, and interacting securely through JWT-based authentication.

---

## Features

- JWT Authentication and Authorization
- Employee Account Management
- Article Creation and Management
- GIF Post Creation and Management
- Article Commenting System
- Role-Based Access Control
- Swagger/OpenAPI Documentation
- MySQL Database Integration

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Swagger/OpenAPI

---

## Installation

### Clone the Repository

```bash
git clone https://github.com/steverado9/TeamWorkApi.git
cd TeamWorkApi
```

### Configure Database

Update your `application.yml` file:

```properties
spring:
 application:
  name: TeamWorkApi
 datasource:
  url: jdbc:mysql://localhost:3306/dataBaseName?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false
  username: 
  password: 
 jpa:
 properties:
  hibernate:
   dialect: org.hibernate.dialect.MySQLDialect
 hibernate:
  ddl-auto: update
logging:
 level:
  org:
   springframework:
    security: DEBUG

```

### Configure JWT in Enivironment variable

```properties
jwt.secret=your-secret-key
jwt.expiration=86400000
```

### Run the Application

```text
 click run in TeamWorkApiApplication
```

The application will start on:

```text
http://localhost:8080
```

---

## API Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI Specification:

```text
http://localhost:8080/v3/api-docs
```

---

## Authentication

### Login

**Endpoint**

```http
POST /auth/login
```

### Request

```json
{
  "email": "employee@example.com",
  "password": "password123"
}
```

### Response

```json
{
  "status": "success",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

## Authorization

For protected endpoints, include the JWT token in the Authorization header:

```http
Authorization: Bearer <your-jwt-token>
```

Example:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

# API Endpoints

## Authentication

| Method | Endpoint | Description |
|----------|----------|----------|
| POST | `/auth/login` | Authenticate employee |

---

## Articles

| Method | Endpoint | Description |
|----------|----------|----------|
| POST | `/articles` | Create an article |
| GET | `/articles/{articleId}` | View article by ID |
| PUT | `/articles/{articleId}` | Update an article |
| DELETE | `/articles/{articleId}` | Delete an article |
| POST | `/articles/{articleId}/comment` | Comment on an article |

---

## GIFs

| Method | Endpoint             | Description |
|----------|----------------------|----------|
| POST | `/gifs`              | Create a GIF post |
| GET | `/gifs/{id}`         | View GIF by ID |
| DELETE | `/gifs/{id}`         | Delete a GIF |
| POST | `/gifs/{id}/comment` | Comment on an article |


## Feed

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/feed`  | View Feed   |

---

## HTTP Status Codes

| Status Code | Description |
|------------|-------------|
| 200 | OK |
| 201 | Created |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 500 | Internal Server Error |

---

## Author

**Isaac Stephen**

Built with Spring Boot, Spring Security, and JWT Authentication.