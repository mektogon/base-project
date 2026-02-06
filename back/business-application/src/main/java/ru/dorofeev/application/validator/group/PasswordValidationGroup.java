package ru.dorofeev.application.validator.group;

import jakarta.validation.groups.Default;

/**
 * Группа валидации пароля.
 */
public interface PasswordValidationGroup {

    /**
     * Аутентификация.
     */
    interface OnAuthenticate extends Default {

    }

    /**
     * Регистрация пользователя.
     */
    interface OnRegistration extends Default {

    }

    /**
     * Сброс.
     */
    interface OnReset extends Default {

    }

    /**
     * Изменение.
     */
    interface OnChange extends Default {

    }
}
