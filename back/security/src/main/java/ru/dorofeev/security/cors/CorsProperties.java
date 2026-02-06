package ru.dorofeev.security.cors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Параметры для CORS.
 */
@Setter
@Getter
@Validated
@Configuration
@ConfigurationProperties(prefix = "security.cors")
public class CorsProperties {

    /**
     * Список разрешенных источников.
     */
    private List<String> allowedOrigins;

    /**
     * Список разрешенных HTTP-методов.
     */
    private List<String> allowedMethods;

    /**
     * Список разрешенных заголовков.
     */
    private List<String> allowedHeaders;
}
