package ru.dorofeev;

import com.redis.testcontainers.RedisContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import ru.dorofeev.application.Application;

@Slf4j
@TestConfiguration
public class ApplicationTest {

    @SuppressWarnings("resource")
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

    @Bean
    @ServiceConnection
    public RedisContainer redisContainer() {
        return new RedisContainer(DockerImageName.parse("redis:8.4.0-alpine"));
    }

    public static void main(String[] args) {
        SpringApplication
                .from(Application::main)
                .with(ApplicationTest.class)
                .run(args);
    }
}