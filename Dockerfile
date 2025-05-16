# Etapa de construcción
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Etapa final
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/quotationsystem-0.0.1-SNAPSHOT.jar quotationsystem.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "quotationsystem.jar"]