# 🕘 Time-tracker

A Spring Boot-based Java web application that provides real-time time tracking functionality.

## 📦 Key Features:

- Automatic Time Recording: Continuously writes current timestamp to PostgreSQL database every second
- RESTful Web Interface: Provides HTTP endpoints to retrieve stored time entries in JSON format in ascending order
- Fault-Tolerant Database Connectivity: resilient connection handling with automatic retry mechanism
- Data Integrity: Ensures chronological data ordering without using database sorting or indexes
- Connection Recovery: Automatically detects database outages and attempts reconnection
- Data Persistence: Buffers and replays missed time entries when database connection is restored

## 🛠️Technical Architecture:

Java 21, Spring (Boot, Data, MVC), Liquibase, MapStruct, Springdoc, PostgerSQL, Cucumber, JUnit, Assertj, H2, Maven,
Docker, Docker-Compose

## ▶️ How to Run:

### Local Setup

RUN Postgrest DB in Docker container
> dc up time-tracker-db

RUN Application
> ./mvnw spring-boot:run

You can use key `-P` to run with some profile:

    -Pdev
    -Ptest
    -Pdocker

### With Docker

    docker-compose up --build

# ✅ Access service at:

http://localhost:8080/swagger-ui/index.html – Swagger UI Api
