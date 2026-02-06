package ru.dorofeev.application.ufs.authentication.service;

import ru.dorofeev.application.ufs.authentication.model.request.UfsChangePasswordRequest;
import ru.dorofeev.application.ufs.authentication.model.request.UfsLoginRequest;
import ru.dorofeev.application.ufs.authentication.model.request.UfsRegistrationRequest;
import ru.dorofeev.application.ufs.authentication.model.response.UfsAuthenticationResponse;

/**
 * Класс-прокси, см. подробнее : {@link ru.dorofeev.security.authentication.service.AuthenticationService}
 */
public interface AuthenticationServiceProxy {

    /**
     * См. подробнее : {@link ru.dorofeev.security.authentication.service.AuthenticationService#login(LoginRequest)}
     */
    UfsAuthenticationResponse login(UfsLoginRequest request);

    /**
     * См. подробнее : {@link ru.dorofeev.security.authentication.service.AuthenticationService#logout()}
     */
    void logout();

    /**
     * См. подробнее : {@link ru.dorofeev.security.authentication.service.AuthenticationService#changePassword(ChangePasswordRequest)}
     */
    void changePassword(UfsChangePasswordRequest request);

    /**
     * См. подробнее : {@link ru.dorofeev.security.authentication.service.AuthenticationService#resetPassword(LoginRequest)}
     */
    void resetPassword(UfsLoginRequest request);

    /**
     * См. подробнее : {@link ru.dorofeev.security.authentication.service.AuthenticationService#registration(RegistrationRequest)}
     */
    UfsAuthenticationResponse registration(UfsRegistrationRequest request);
}
