package ru.dorofeev.security.authentication.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.dorofeev.security.authentication.exception.SecurityException;
import ru.dorofeev.security.authentication.exception.enums.ErrorType;

import java.util.Objects;

@UtilityClass
public final class AuthenticationUtils {

    /**
     * Проверка аутентификации пользователя.
     * Если пользователь аутентифицирован, то будет выброшено {@link SecurityException}
     * с типом {@link ErrorType#USER_ALREADY_AUTHENTICATED}.
     * Анонимный пользователь считается НЕ аутентифицированным.
     */
    public static void isAuthenticatedThenThrowException() {
        if (isAuthenticated()) {
            throw new SecurityException(ErrorType.USER_ALREADY_AUTHENTICATED);
        }
    }

    /**
     * Проверка аутентификации пользователя.
     * Если пользователь НЕ аутентифицирован, то будет выброшено {@link SecurityException}
     * с типом {@link ErrorType#USER_NOT_AUTHENTICATED}.
     * Анонимный пользователь считается НЕ аутентифицированным.
     */
    public static void isNotAuthenticatedThenThrowException() {
        if (!isAuthenticated()) {
            throw new SecurityException(ErrorType.USER_NOT_AUTHENTICATED);
        }
    }

    /**
     * Проверка аутентификации пользователя.
     * Анонимный пользователь считается НЕ аутентифицированным.
     *
     * @return true/false в зависимости от аутентификации.
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated()) {
            return !(authentication instanceof AnonymousAuthenticationToken);
        }

        return false;
    }
}
