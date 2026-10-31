FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY merge-sort-library /app/merge-sort-library
RUN mvn -f /app/merge-sort-library/pom.xml clean install -DskipTests
COPY auth-service /app/auth-service
RUN mvn -f /app/auth-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/auth-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]