# Build stage
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app
# Copy project into container /app folder
COPY .. .
# package JAR (will be in /app/target/)
RUN ./mvnw package -DskipTests

# Run stage
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
# Copy JAR from folder target/ that exists in builder container
COPY --from=builder /app/target/time-tracker-*.jar time-tracker.jar
# Make app port available to the world outside this container
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "time-tracker.jar"]
