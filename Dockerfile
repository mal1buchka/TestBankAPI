FROM openjdk:17-jdk-alpine
LABEL authors = "malibuchka"
COPY target/TipaBankAPI.jar .
ENTRYPOINT ["java", "-Xms1024m", "-Xmx1500m", "-jar", "TipaBankAPI.jar"]
