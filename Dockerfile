# Start from an OpenJDK base image
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy the built jar to the container
COPY target/society-entry-auth-service-0.0.1.jar app.jar

# Set default active profile
ENV SPRING_PROFILES_ACTIVE=prod

# Expose port
EXPOSE 8080

# Set the entry point
ENTRYPOINT ["java", "-jar", "app.jar"]