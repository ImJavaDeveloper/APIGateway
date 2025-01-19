# Use a base image for Java runtime
FROM openjdk:21-jdk

# Set the working directory inside the container
WORKDIR /Users/rahatali/Desktop

# Copy the compiled Spring Boot jar file into the container
COPY target/APIGateway-0.0.1-SNAPSHOT.jar APIGateway.jar

# Expose the port on which the application runs
EXPOSE 8086

# Command to run the application
ENTRYPOINT ["java", "-jar", "APIGateway.jar"]