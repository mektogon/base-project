package ru.dorofeev.security.authentication.service;


import ru.dorofeev.security.authentication.model.request.ChangePasswordRequest;
import ru.dorofeev.security.authentication.model.request.LoginRequest;
import ru.dorofeev.security.authentication.model.request.RegistrationRequest;
import ru.dorofeev.security.authentication.model.response.AuthenticationResponse;

/**
 * Контракт для взаимодействия с аутентификацией.
 */
public interface AuthenticationService {

    /**
     * Аутентификация пользователя.
     *
     * @param request параметры аутентификации.
     * @return объект типа {@link AuthenticationResponse}.
     */
    AuthenticationResponse login(LoginRequest request);

    /**
     * Выход пользователя из системы.
     */
    void logout();

    /**
     * Изменение пароля пользователя.
     *
     * @param request параметры изменения пароля.
     */
    void changePassword(ChangePasswordRequest request);

    /**
     * Сброс пароля у пользователя.
     *
     * @param request параметры сброса.
     */
    void resetPassword(LoginRequest request);

    /**
     * Регистрация пользователя.
     *
     * @param request параметры регистрации.
     * @return объект типа {@link AuthenticationResponse}.
     */
    AuthenticationResponse registration(RegistrationRequest request);
}
