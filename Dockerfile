FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
LABEL authors="malibuchka"
WORKDIR /app
COPY --from=builder /app/target/TipaBankAPI.jar app.jar
ENTRYPOINT ["java", "-Xms1024m", "-Xmx1500m", "-jar", "app.jar"]
