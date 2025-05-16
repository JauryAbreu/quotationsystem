# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa final
FROM eclipse-temurin:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/quotationsystem-0.0.1-SNAPSHOT.jar quotationsystem.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "quotationsystem.jar"]