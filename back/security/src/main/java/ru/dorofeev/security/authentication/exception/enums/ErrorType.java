package ru.dorofeev.security.authentication.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Типы ошибок для модуля безопасности.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorType {

    //region Ошибки с пользователем.

    /**
     * Не удалось найти пользователя.
     */
    USER_NOT_FOUND("S1000"),

    /**
     * Аккаунт заблокирован.
     */
    USER_BLOCKED("S1001"),

    /**
     * Аккаунт удален.
     */
    USER_DELETED("S1002"),

    /**
     * Время действия аккаунта истекло.
     */
    USER_EXPIRED("S1003"),

    /**
     * Пользователь не аутентифицирован.
     */
    USER_NOT_AUTHENTICATED("S1004"),

    /**
     * Пользователь с указанным E-mail уже существует.
     */
    USER_ALREADY_EXIST_BY_EMAIL("S1005"),

    /**
     * Пользователь с указанным номером телефона уже существует.
     */
    USER_ALREADY_EXIST_BY_PHONE_NUMBER("S1006"),

    /**
     * Не удалось зарегистрировать пользователя.
     */
    USER_NOT_REGISTERED("S1007"),

    /**
     * Пользователь уже аутентифицирован.
     */
    USER_ALREADY_AUTHENTICATED("S1008"),

    /**
     * У пользователя недостаточно прав.
     */
    USER_NOT_HAVE_ENOUGH_RIGHTS("S1009"),

    //endregion

    //region Ошибки с паролем.

    /**
     * Неправильный пароль.
     */
    PASSWORD_WRONG("S2000"),

    /**
     * Пароль истек.
     */
    PASSWORD_EXPIRED("S2001"),

    /**
     * Пароль был использован ранее.
     */
    PASSWORD_REPEATED("S2002"),

    //endregion

    //region Ошибки с сессией.

    /**
     * Не удалось найти сессию.
     */
    SESSION_NOT_FOUND("S3000"),

    //endregion

    ;

    /**
     * Код ошибки, выводимый пользователю.
     */
    private final String code;
}
