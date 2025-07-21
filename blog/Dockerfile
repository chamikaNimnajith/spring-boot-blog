# Use an official Maven image to build the Spring Boot app
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies (offline cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official OpenJDK slim image for runtime
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=build /app/target/blog-0.0.1-SNAPSHOT.jar ./app.jar

# Expose the application port
EXPOSE 8080

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
