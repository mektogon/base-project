package ru.dorofeev.application;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Base Project",
                description = "Шаблонный проект для быстрого старта разработки приложения",
                version = "01.000.00"
        ),
        servers = @Server(
                url = "{protocol}://{host}:{port}/",
                description = "Адрес приложения",
                variables = {
                        @ServerVariable(
                                name = "protocol", defaultValue = "http"
                        ),
                        @ServerVariable(
                                name = "host", defaultValue = "localhost"
                        ),
                        @ServerVariable(
                                name = "port", defaultValue = "8081"
                        )
                }
        )
)
@SpringBootApplication(
        scanBasePackages = {
                "ru.dorofeev"
        }
)
public class  Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
