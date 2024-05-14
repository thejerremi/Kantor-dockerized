# syntax=docker/dockerfile:1
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Fix line endings and make the script executable (important for Windows-hosted volumes)
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

RUN ./mvnw dependency:go-offline

CMD ["./mvnw", "spring-boot:run"]
