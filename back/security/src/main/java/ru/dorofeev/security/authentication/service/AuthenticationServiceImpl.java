package ru.dorofeev.security.authentication.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.dorofeev.SecurityProperties;
import ru.dorofeev.database.entity.UserInfoEntity;
import ru.dorofeev.database.repository.UserInfoRepository;
import ru.dorofeev.security.authentication.exception.SecurityException;
import ru.dorofeev.security.authentication.exception.enums.ErrorType;
import ru.dorofeev.security.authentication.model.SecurityUserDetails;
import ru.dorofeev.security.authentication.model.request.ChangePasswordRequest;
import ru.dorofeev.security.authentication.model.request.LoginRequest;
import ru.dorofeev.security.authentication.model.request.RegistrationRequest;
import ru.dorofeev.security.authentication.model.response.AuthenticationResponse;
import ru.dorofeev.security.authentication.utils.AuthenticationUtils;
import ru.dorofeev.security.configuration.PepperedBCryptPasswordEncoder;
import ru.dorofeev.security.cookie.service.CookieService;
import ru.dorofeev.security.passwordhistory.service.PasswordHistoryService;
import ru.dorofeev.security.session.service.SessionService;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final SecurityProperties securityProperties;

    private final PepperedBCryptPasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final PasswordHistoryService passwordHistoryService;
    private final SessionService sessionService;
    private final CookieService cookieService;

    private final UserInfoRepository userInfoRepository;

    @Transactional
    @Override
    public AuthenticationResponse registration(RegistrationRequest request) {

        AuthenticationUtils.isAuthenticatedThenThrowException();

        String email = request.getLogin();
        if (userInfoRepository.existByEmail(email)) {
            throw new SecurityException(ErrorType.USER_ALREADY_EXIST_BY_EMAIL);
        }

        String phoneNumber = request.getPhoneNumber();
        if (Objects.nonNull(phoneNumber) && userInfoRepository.existByPhoneNumber(phoneNumber)) {
            throw new SecurityException(ErrorType.USER_ALREADY_EXIST_BY_PHONE_NUMBER);
        }

        UserInfoEntity savedUser;
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        try {
            savedUser = userInfoRepository.saveAndFlush(
                    UserInfoEntity.builder()
                            .email(email)
                            .passwordHash(encodedPassword)
                            .firstName(request.getFirstName())
                            .middleName(request.getMiddleName())
                            .lastName(request.getLastName())
                            .phoneNumber(phoneNumber)
                            .passwordExpirationDate(
                                    LocalDate.now().plusDays(
                                            securityProperties.getPassword().getCountDaysPasswordExpire()
                                    )
                            )
                            .build()
            );
        } catch (Exception ex) {
            log.error("Не удалось зарегистрировать пользователя! ", ex);
            throw new SecurityException(ErrorType.USER_NOT_REGISTERED);
        }

        passwordHistoryService.savePasswordHash(savedUser.getId(), encodedPassword);

        return AuthenticationResponse.builder()
                .firstName(savedUser.getFirstName())
                .middleName(savedUser.getMiddleName())
                .build();
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        AuthenticationUtils.isAuthenticatedThenThrowException();

        String login = request.getLogin();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, request.getPassword())
        );

        UserInfoEntity user = ((SecurityUserDetails) authentication.getPrincipal()).userInfo();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        authentication.getName(),
                        null,
                        authentication.getAuthorities()
                )
        );

        sessionService.initialSessionContext(
                authentication,
                Map.of(securityProperties.getSession().getRememberMe().getAttributeName(), request.isRememberMe())
        );
        cookieService.upsertCsrfCookieToken();

        userInfoRepository.updateLastLoginDateTimeByEmail(login);

        return AuthenticationResponse.builder()
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .build();
    }

    @Override
    public void logout() {
        sessionService.invalidateSession();
        SecurityContextHolder.clearContext();
        cookieService.clearAppCookies();
    }

    @Transactional
    @Override
    public void changePassword(ChangePasswordRequest request) {

        AuthenticationUtils.isNotAuthenticatedThenThrowException();

        UserInfoEntity userInfoEntity = userInfoRepository.findByEmail(request.getLogin())
                .orElseThrow(() -> new SecurityException(ErrorType.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), userInfoEntity.getPasswordHash())) {
            throw new SecurityException(ErrorType.PASSWORD_WRONG);
        }

        UUID userId = userInfoEntity.getId();
        String rawPassword = request.getNewPassword();
        String encodedNewPassword = passwordEncoder.encode(rawPassword);

        if (passwordHistoryService.checkRawPassword(userId, rawPassword)) {
            throw new SecurityException(ErrorType.PASSWORD_REPEATED);
        }

        userInfoEntity.setPasswordHash(encodedNewPassword);
        userInfoRepository.save(userInfoEntity);

        passwordHistoryService.savePasswordHash(userId, encodedNewPassword);

        logout();
    }

    @Override
    public void resetPassword(LoginRequest request) {
        AuthenticationUtils.isAuthenticatedThenThrowException();
        throw new UnsupportedOperationException("Реализация в рамках AS-25 (отв. Дорофеев)");
    }
}
