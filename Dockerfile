# Multi-stage build for the Pharmacy Application

# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copy the Maven wrapper and POM file
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Add a non-root user to run the application
RUN addgroup -S pharmacy && adduser -S pharmacy -G pharmacy
USER pharmacy:pharmacy

# Copy the built JAR file from the build stage
COPY --from=build /app/target/Pharmacy-0.0.1-SNAPSHOT.jar app.jar

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS=""

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]