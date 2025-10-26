package ru.dorofeev;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import ru.dorofeev.application.Application;

@Slf4j
@TestConfiguration
public class ApplicationTest {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:18.0")
                .withDatabaseName("test_db")
                .withUsername("test")
                .withPassword("test")
                .withLogConsumer(new Slf4jLogConsumer(log))
                .waitingFor(Wait.forListeningPort());

        postgresContainer.start();
        return postgresContainer;
    }

    public static void main(String[] args) {
        SpringApplication
                .from(Application::main)
                .with(ApplicationTest.class)
                .run(args);
    }
}