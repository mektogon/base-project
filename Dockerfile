FROM eclipse-temurin:21-jre

COPY ./back/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]