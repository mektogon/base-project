package ru.dorofeev.security.session;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Map;

/**
 * Базовые параметры для сессии.
 */
@Setter
@Getter
@Validated
@Configuration
@ConfigurationProperties("security.session")
public class SessionProperties {

    /**
     * Параметры настройки предела одновременных сеансов по каналам.
     */
    @NotNull
    private LimitPerChannelProperties limitPerChannel;

    /**
     * Параметры для настройки функционала "Запомнить меня".
     */
    @NotNull
    private RememberMeProperties rememberMe;

    @Getter
    @Setter
    @Validated
    @Configuration
    @ConfigurationProperties("security.session.limit-per-channel")
    public static class LimitPerChannelProperties {

        /**
         * Наименование атрибута сессии, используемого для хранения канала HTTP-запроса.
         */
        @NotBlank
        private String attributeName;

        /**
         * Карта состояний
         */
        @NotEmpty
        private Map<String, Integer> limitMap;
    }

    @Getter
    @Setter
    @Validated
    @Configuration
    @ConfigurationProperties("security.session.remember-me")
    public static class RememberMeProperties {

        /**
         * Наименование атрибута сессии, используемого для хранения признака "Запомни меня".
         */
        @NotBlank
        private String attributeName;

        /**
         * Время жизни сессии при включенном флаге remember-me.
         */
        @NotNull
        private Duration maxAge;
    }
}
