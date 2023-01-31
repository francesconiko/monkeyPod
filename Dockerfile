# Use openjdk 17 as base image
FROM openjdk:17-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the compiled jar file to the container
COPY target/*.jar /app/chaosmonkeypod.jar

# Set the environment variables
ENV JAVA_OPTS=""

# Command to run the jar file
CMD ["java", "-jar", "caosmonkeypod.jar"]