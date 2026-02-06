package ru.dorofeev.application.ufs.authentication.mapper;

import org.mapstruct.Mapper;
import ru.dorofeev.application.ufs.authentication.model.request.UfsChangePasswordRequest;
import ru.dorofeev.application.ufs.authentication.model.request.UfsLoginRequest;
import ru.dorofeev.application.ufs.authentication.model.request.UfsRegistrationRequest;
import ru.dorofeev.application.ufs.authentication.model.response.UfsAuthenticationResponse;
import ru.dorofeev.security.authentication.model.request.ChangePasswordRequest;
import ru.dorofeev.security.authentication.model.request.LoginRequest;
import ru.dorofeev.security.authentication.model.request.RegistrationRequest;
import ru.dorofeev.security.authentication.model.response.AuthenticationResponse;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {

    UfsAuthenticationResponse map(AuthenticationResponse source);

    ChangePasswordRequest map(UfsChangePasswordRequest source);

    RegistrationRequest map(UfsRegistrationRequest source);

    LoginRequest map(UfsLoginRequest source);
}
