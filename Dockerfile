# Stage 1: Build the application
FROM gradle:8.3-jdk17-alpine AS builder

WORKDIR /app

# Copy only the necessary files for dependency resolution
COPY build.gradle settings.gradle /app/
COPY gradle /app/gradle

# Download dependencies
RUN gradle --version

# Copy the remaining files
COPY . .

# Build the application 'todo activate tests'
RUN gradle build -x test

# Stage 2: Use a smaller base image for the final image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the JAR from the build stage
COPY --from=builder /app/build/libs/myclub-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
