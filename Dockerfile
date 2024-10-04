FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw package -DskipTests

RUN mv target/demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]