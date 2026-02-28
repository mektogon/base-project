package ru.dorofeev.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import ru.dorofeev.security.cookie.CookieProperties;
import ru.dorofeev.security.cors.CorsProperties;
import ru.dorofeev.security.session.SessionProperties;

import java.util.List;

/**
 * Пользовательские настройки модуля безопасности.
 */
@Setter
@Getter
@Validated
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    /**
     * Режим работы модуля безопасности.
     */
    private String mode;

    /**
     * Настройки политики паролей.
     */
    @NotNull
    private PasswordProperties password;

    /**
     * Параметры endpoint-ов.
     */
    @NotNull
    private EndpointProperties endpoint;

    /**
     * Параметры для CORS (Cross-Origin Resource Sharing).
     */
    @NotNull
    private CorsProperties cors;

    /**
     * Параметры сессии.
     */
    @NotNull
    private SessionProperties session;

    /**
     * Общие настройки cookie.
     */
    @NotNull
    private CookieProperties cookie;

    @Getter
    @Setter
    @Validated
    @Configuration
    public static class PasswordProperties {

        /**
         * Длительность пароля в днях до его истечения.
         */
        @NotNull
        private Integer countDaysPasswordExpire;

        /**
         * Количество итераций хеширования пароля.
         */
        @Range(min = 12)
        private Integer strength;

        /**
         * "Перец", применяемый при хешировании пароля.
         */
        @NotBlank
        private String pepper;

        /**
         * Алгоритм шифрования.
         */
        @NotBlank
        private String algorithm;

        /**
         * Параметры для истории паролей.
         */
        @NotNull
        private PasswordHistoryProperties history;

        @Getter
        @Setter
        public static class PasswordHistoryProperties {

            /**
             * Количество паролей, хранимых в истории.
             */
            @NotNull
            private int depth;
        }
    }

    @Getter
    @Setter
    @Validated
    @Configuration
    public static class EndpointProperties {

        /**
         * Список endpoint-ов доступных без аутентификации.
         */
        private List<String> whiteList;
    }
}
