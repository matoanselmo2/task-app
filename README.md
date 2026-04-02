# Task App

A RESTful task management API built with **Spring Boot**, **Spring Security**, and **MongoDB**. Users can register, log in, and manage their personal to-do tasks with full CRUD support.

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.0.5 |
| Language | Java 21 |
| Database | MongoDB |
| Security | Spring Security + BCrypt |
| Session | HTTP Session (cookie-based) |
| Build | Gradle (Kotlin DSL) |

## Features

- User registration and login with BCrypt-hashed passwords
- Session-based authentication
- Create, read, rename, complete, and delete tasks
- Tasks are user-scoped — users can only access their own tasks

## Prerequisites

- Java 21+
- A running MongoDB instance

## Configuration

Update the MongoDB connection URI in `src/main/resources/application.properties` (or `application.yml`) to point to your MongoDB instance. The default connection string in the source is:

```
mongodb://admin:admin@192.168.1.243:27017
```

## Running the App

```bash
./gradlew bootRun
```

The server starts on `http://localhost:8080` by default.

## API Reference

### Authentication

| Method | Endpoint | Description | Request Body |
|---|---|---|---|
| `POST` | `/api/auth/createUser` | Register a new user | `{"name": "...", "password": "..."}` |
| `POST` | `/api/auth/login` | Log in and create a session | `{"name": "...", "password": "..."}` |

### Tasks

All task endpoints require an active session (obtained via `/api/auth/login`).

| Method | Endpoint | Description | Request Body |
|---|---|---|---|
| `POST` | `/api/tasks` | Create a new task | `{"title": "..."}` |
| `GET` | `/api/tasks` | List all tasks for the logged-in user | — |
| `GET` | `/api/tasks/{id}` | Get a single task by ID | — |
| `PATCH` | `/api/tasks/{id}/rename` | Rename a task | `{"title": "..."}` |
| `PATCH` | `/api/tasks/{id}/complete` | Mark a task as completed | — |
| `DELETE` | `/api/tasks/{id}/delete` | Delete a task | — |

### Status Codes

| Code | Meaning |
|---|---|
| `200` | OK |
| `201` | Created |
| `401` | Invalid credentials |
| `403` | Not logged in, or task belongs to another user |
| `404` | Task not found |
| `409` | Username or task title already exists |

## Project Structure

```
src/main/java/me/mato/app/
├── TaskApplication.java          # Entry point
├── HomeController.java           # GET /
├── exception/
│   ├── ExceptionController.java  # Global error handler (@ControllerAdvice)
│   ├── InvalidCredentialsException.java
│   ├── TaskNotFoundException.java
│   └── UnauthorizedException.java
├── security/
│   └── SecurityConfig.java       # BCrypt bean, security filter chain
├── user/
│   ├── controller/UserController.java
│   ├── model/User.java           # MongoDB document (collection: users)
│   ├── repository/UserRepository.java
│   └── service/UserService.java
└── task/
    ├── controller/TaskController.java
    ├── model/Task.java            # MongoDB document (collection: tasks)
    ├── repository/TaskRepository.java
    └── service/TaskService.java
```

## Data Models

### User

```json
{
  "_id": "ObjectId",
  "name": "string (unique)",
  "hashedPassword": "string (BCrypt)"
}
```

### Task

```json
{
  "_id": "ObjectId",
  "ownerId": "ObjectId",
  "title": "string",
  "completed": "boolean",
  "createdAt": "ISO 8601 datetime"
}
```
