package ru.dorofeev.security.cors;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

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
    private Set<String> allowedOrigins;

    /**
     * Список разрешенных HTTP-методов.
     */
    private Set<String> allowedMethods;

    /**
     * Список разрешенных заголовков.
     */
    private Set<String> allowedHeaders;
}
