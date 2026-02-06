package ru.dorofeev.application.ufs.authentication.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dorofeev.application.ufs.authentication.mapper.AuthenticationMapper;
import ru.dorofeev.application.ufs.authentication.model.request.UfsChangePasswordRequest;
import ru.dorofeev.application.ufs.authentication.model.request.UfsLoginRequest;
import ru.dorofeev.application.ufs.authentication.model.request.UfsRegistrationRequest;
import ru.dorofeev.application.ufs.authentication.model.response.UfsAuthenticationResponse;
import ru.dorofeev.security.authentication.service.AuthenticationService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceProxyImpl implements AuthenticationServiceProxy {

    private final AuthenticationService delegate;
    private final AuthenticationMapper mapper;

    @Override
    public UfsAuthenticationResponse login(UfsLoginRequest request) {
        return mapper.map(delegate.login(mapper.map(request)));
    }

    @Override
    public void logout() {
        delegate.logout();
    }

    @Override
    public void changePassword(UfsChangePasswordRequest request) {
        delegate.changePassword(mapper.map(request));
    }

    @Override
    public void resetPassword(UfsLoginRequest request) {
        delegate.resetPassword(mapper.map(request));
    }

    @Override
    public UfsAuthenticationResponse registration(UfsRegistrationRequest request) {
        return mapper.map(delegate.registration(mapper.map(request)));
    }
}
