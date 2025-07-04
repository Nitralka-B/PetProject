FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -pl authorization -am -DskipTests
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/authorization/target/authorization-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]
 