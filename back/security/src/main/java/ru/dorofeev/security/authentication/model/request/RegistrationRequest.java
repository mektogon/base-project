package ru.dorofeev.security.authentication.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Модель запроса с параметрами регистрации пользователя.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest extends LoginRequest {

    /**
     * Имя пользователя.
     */
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    private String lastName;

    /**
     * Отчество пользователя.
     */
    private String middleName;

    /**
     * Телефон пользователя.
     */
    private String phoneNumber;
}
