package ru.dorofeev.security.authentication.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Модель ответа аутентифицированного пользователя.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    /**
     * Имя пользователя.
     */
    private String firstName;

    /**
     * Отчество пользователя.
     */
    private String middleName;
}
