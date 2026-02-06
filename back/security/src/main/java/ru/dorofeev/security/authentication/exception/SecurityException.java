package ru.dorofeev.security.authentication.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.dorofeev.security.authentication.exception.enums.ErrorType;

/**
 * Базовый тип исключения для модуля безопасности.
 */
@Getter
@RequiredArgsConstructor
public class SecurityException extends RuntimeException {

    /**
     * Тип ошибки {@link ErrorType}.
     */
    private final ErrorType type;
}
