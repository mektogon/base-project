FROM eclipse-temurin:20.0.2_9-jdk-alpine

COPY ./build/libs/*.war app.war

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]