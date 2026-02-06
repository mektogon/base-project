package ru.dorofeev.application.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Логирование входящих запросов. <br/>
 * Для вывода логов необходимо выставить уровень DEBUG для "org.springframework.web.filter.CommonsRequestLoggingFilter"
 */
@Configuration
public class RequestLoggingConfiguration extends CommonsRequestLoggingFilter {

    private static final String PASSWORD_PATTERN =  "\"(password|newPassword)\"\\s*:\\s*\"[^\"]*\"";
    private static final String PASSWORD_REPLACEMENT = "\"$1\": \"******\"";

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter() {
            @Override
            protected @NotNull String createMessage(@NotNull HttpServletRequest request, @NotNull String prefix, @NotNull String suffix) {
                String message = super.createMessage(request, prefix, suffix);

                if (StringUtils.isEmpty(message)) {
                    return message;
                }

                return message.replaceAll(
                        PASSWORD_PATTERN,
                        PASSWORD_REPLACEMENT
                );
            }
        };

        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(true);
        loggingFilter.setMaxPayloadLength(64000);
        return loggingFilter;
    }
}
