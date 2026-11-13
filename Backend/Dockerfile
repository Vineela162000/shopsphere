# Base image: a small Linux with Java 17 runtime (JRE) already installed.
# eclipse-temurin = a free, popular Java build that companies use.
FROM eclipse-temurin:17-jre

# Working folder inside the container (like "cd /app")
WORKDIR /app

# Copy our fat JAR from the Mac into the image, and rename it to app.jar
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar

# Note for humans: this app listens on port 8080
EXPOSE 8080

# The command that runs when the container starts (same as we did by hand)
ENTRYPOINT ["java", "-jar", "app.jar"]