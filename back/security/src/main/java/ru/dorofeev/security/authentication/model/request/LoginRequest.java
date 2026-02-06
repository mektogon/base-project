package ru.dorofeev.security.authentication.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Модель запроса с параметрами аутентификации.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * Логин пользователя. (E-mail)
     */
    private String login;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Признак 'Запомнить меня'.
     */
    private boolean rememberMe;
}
