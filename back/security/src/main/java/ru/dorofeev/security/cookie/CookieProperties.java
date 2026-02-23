package ru.dorofeev.security.cookie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import ru.dorofeev.security.SecurityProperties;
import ru.dorofeev.security.cookie.filter.CookieRefreshFilter;

import java.time.Duration;
import java.util.Set;

/**
 * Базовые параметры для cookie.
 */
@Setter
@Getter
@Validated
@Configuration
@ConfigurationProperties("security.cookie")
public class CookieProperties {

    /**
     * Настройки CSRF-токена cookie.
     */
    @NotNull
    private CsrfCookieProperties csrf;

    /**
     * Параметры настройки {@link CookieRefreshFilter}.
     */
    @NotNull
    private CookieRefreshFilterProperties cookieRefreshFilter;

    @Getter
    @Setter
    @Validated
    @Configuration
    @ConfigurationProperties("security.cookie.cookie-refresh-filter")
    public static class CookieRefreshFilterProperties {

        /**
         * Список endpoint-ов, которые следует исключить из обработки фильтром. <br/>
         * WARNING: Помимо явного указания исключаются из проверки endpoint-ы из WL.
         */
        private Set<String> excludePaths;

        /**
         * Список наименований cookie, которые подлежат обновлению.
         * Формируется путем сборки из candidateToRefresh
         * и автоматического добавления значений из {@link SecurityProperties.EndpointProperties#getWhiteList()}.
         */
        @NotNull
        private Set<String> candidateToRefresh;
    }

    @Getter
    @Setter
    @Validated
    @Configuration
    @ConfigurationProperties("security.cookie.csrf")
    public static class CsrfCookieProperties extends BaseCookieProperties {

    }

    @Getter
    @Setter
    @Validated
    @Configuration
    @ConfigurationProperties("server.servlet.session.cookie")
    public static class SessionCookieProperties extends BaseCookieProperties {

    }

    @Getter
    @Setter
    private static class BaseCookieProperties {

        /**
         * Наименование cookie HTTP-запроса, в которой приходит CSRF-токен.
         */
        @NotBlank
        private String name;

        /**
         * Длительность жизни cookie.
         */
        @NotNull
        private Duration maxAge;

        /**
         * Путь присваивания cookie.
         */
        @NotBlank
        private String path;

        /**
         * Доступ к cookie из JavaScript на клиентской стороне.
         */
        @NotNull
        private boolean httpOnly;

        /**
         * Передача cookie по HTTPS.
         */
        @NotNull
        private boolean secure;

        /**
         * Передача cookie с cross-site запросами.
         */
        @NotBlank
        private String sameSite;
    }
}
