FROM gradle:jdk21-alpine AS builder

COPY ../../../back back
COPY ../../../docs docs
COPY ../../../build.gradle.kts settings.gradle.kts lombok.config ./

RUN gradle dependencies --no-daemon

RUN gradle bootJar --no-daemon --stacktrace \
    && ls -la /home/gradle/back/build/libs/

FROM eclipse-temurin:21-jre-jammy

WORKDIR /application

RUN adduser --system runner \
    && addgroup --system spring-boot-application \
    && usermod -aG spring-boot-application runner \
    && chown -R runner:spring-boot-application /application \
    && chmod 750 /application

USER runner

COPY --from=builder /home/gradle/back/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]