package ru.dorofeev.security.cookie.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Service;
import ru.dorofeev.security.cookie.CookieProperties;
import ru.dorofeev.security.utils.HttpServletUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CookieServiceImpl implements CookieService {

    private final CookieProperties.CsrfCookieProperties csrfCookieProperties;
    private final CsrfTokenRepository csrfTokenRepository;

    @Override
    public void clearAppCookies() {
        HttpServletRequest currentRequest = HttpServletUtils.getCurrentHttpRequest();

        String sessionCookieName = currentRequest.getServletContext()
                .getSessionCookieConfig()
                .getName();

        List<String> cookieNames = new ArrayList<>();
        cookieNames.add(sessionCookieName);

        if (csrfTokenRepository instanceof CookieCsrfTokenRepository) {
            cookieNames.add(csrfCookieProperties.getName());
        }

        CookieClearingLogoutHandler cookieHandler = new CookieClearingLogoutHandler(
                cookieNames.toArray(new String[0])
        );

        cookieHandler.logout(
                currentRequest,
                HttpServletUtils.getCurrentHttpResponse(),
                SecurityContextHolder.getContext().getAuthentication()
        );
    }

    @Override
    public void upsertCsrfCookieToken() {

        if (!(csrfTokenRepository instanceof CookieCsrfTokenRepository)) {
            throw new IllegalArgumentException(
                    "Данная реализация CSRF-репозитория не работает с cookies! Текущий экземпляр: %s"
                            .formatted(csrfTokenRepository.getClass())
            );
        }

        HttpServletRequest currentRequest = HttpServletUtils.getCurrentHttpRequest();

        CsrfToken newCsrfToken = csrfTokenRepository.generateToken(currentRequest);
        csrfTokenRepository.saveToken(newCsrfToken, currentRequest, HttpServletUtils.getCurrentHttpResponse());
    }
}
