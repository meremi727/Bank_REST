# Build
FROM maven:3.8-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Run
FROM openjdk:17
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080 5005
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]