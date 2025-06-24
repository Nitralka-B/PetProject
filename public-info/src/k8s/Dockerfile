FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY ../../../.. .
RUN mvn clean package -pl public-info -am -DskipTests
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/public-info/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
