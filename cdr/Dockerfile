FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY build/libs/cdr-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]