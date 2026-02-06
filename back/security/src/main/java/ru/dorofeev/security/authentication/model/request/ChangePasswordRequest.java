package ru.dorofeev.security.authentication.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Модель запроса с параметрами изменения пароля.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest extends LoginRequest {

    /**
     * Новый пароль пользователя.
     */
    private String newPassword;
}
