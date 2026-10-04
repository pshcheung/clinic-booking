# Stage 1: Build the application using JDK 26
FROM amazoncorretto:26 AS build
WORKDIR /app

# Copy build files (for Maven, use pom.xml; for Gradle, use build.gradle)
COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

FROM alpine:latest
# Install tar and support for common compression formats
RUN apk add --no-cache tar

# Build and package the application (skipping tests for speed)
# Replace with './gradlew bootJar' or equivalent if using Gradle
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the runtime image
FROM amazoncorretto:26-alpine AS runtime
WORKDIR /app

# Copy the built JAR from the build stage
# Update the JAR name to match your project's output
COPY ./target/*.jar app.jar

# Expose your application's port
EXPOSE 8080

# Run the Java application
ENTRYPOINT ["java", "-jar", "app.jar"]
