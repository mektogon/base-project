package ru.dorofeev.security.cookie.strategy;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.dorofeev.security.cookie.CookieProperties;
import ru.dorofeev.security.cookie.utils.CookieUtils;
import ru.dorofeev.security.session.SessionProperties;

@Component
@RequiredArgsConstructor
public class CsrfCookieRefreshStrategy implements CookieRefreshStrategy {

    private final CookieProperties.CsrfCookieProperties csrfCookieProperties;
    private final SessionProperties.RememberMeProperties rememberMeProperties;

    @Override
    public boolean support(@NotNull String cookieName) {
        return csrfCookieProperties.getName().equals(cookieName);
    }

    @Override
    public void apply(@NotNull Cookie original, @NotNull HttpServletResponse response, boolean rememberMe) {
        response.addCookie(
                CookieUtils.buildCookie(
                        original.getName(),
                        original.getValue(),
                        rememberMe ? rememberMeProperties.getMaxAge() : csrfCookieProperties.getMaxAge(),
                        csrfCookieProperties.getPath(),
                        csrfCookieProperties.isSecure(),
                        csrfCookieProperties.isHttpOnly(),
                        csrfCookieProperties.getSameSite()
                )
        );
    }
}
