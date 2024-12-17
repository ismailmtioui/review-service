# Use official OpenJDK 17 image as a base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged jar file into the container
COPY target/review-service-0.0.1-SNAPSHOT.jar review-service.jar

# Expose the port that the app will run on
EXPOSE 8089

# Run the application
ENTRYPOINT ["java", "-jar", "review-service.jar"]
