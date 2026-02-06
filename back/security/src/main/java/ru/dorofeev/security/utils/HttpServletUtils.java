package ru.dorofeev.security.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.dorofeev.security.authentication.exception.SecurityException;
import ru.dorofeev.security.authentication.exception.enums.ErrorType;

import java.util.Objects;
import java.util.Optional;

@UtilityClass
public final class HttpServletUtils {

    /**
     * Метод получения текущего контекста запроса.
     *
     * @throws IllegalStateException если не удалось получить контекст запроса.
     */
    public static HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (Objects.isNull(attributes)) {
            throw new IllegalStateException("Не удалось получить контекст запроса!");
        }

        return attributes.getRequest();
    }

    /**
     * Метод получения текущего контекста ответа.
     *
     * @throws IllegalStateException если не удалось получить контекст ответа.
     */
    public static HttpServletResponse getCurrentHttpResponse() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (Objects.isNull(attributes)) {
            throw new IllegalStateException("Не удалось получить контекст ответа!");
        }

        return attributes.getResponse();
    }

    /**
     * Получить {@link java.security.Principal} текущего аутентифицированного пользователя.
     *
     * @throws RuntimeException если пользователь не аутентифицирован.
     */
    public static String getCurrentPrincipalName() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .orElseThrow(() -> new SecurityException(ErrorType.USER_NOT_AUTHENTICATED));
    }
}
