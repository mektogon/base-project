package ru.dorofeev.security.cookie.strategy;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.dorofeev.security.cookie.CookieProperties;
import ru.dorofeev.security.cookie.utils.CookieUtils;
import ru.dorofeev.security.session.SessionProperties;

@Component
public class SessionCookieRefreshStrategy implements CookieRefreshStrategy {

    private final CookieProperties.SessionCookieProperties sessionCookieProperties;
    private final SessionProperties.RememberMeProperties rememberMeProperties;

    public SessionCookieRefreshStrategy(CookieProperties.SessionCookieProperties sessionCookieProperties, SessionProperties.RememberMeProperties rememberMeProperties) {
        this.sessionCookieProperties = sessionCookieProperties;
        this.rememberMeProperties = rememberMeProperties;
    }

    @Override
    public boolean support(@NotNull String cookieName) {
        return sessionCookieProperties.getName().equals(cookieName);
    }

    @Override
    public void apply(@NotNull Cookie original, @NotNull HttpServletResponse response, boolean rememberMe) {
        response.addCookie(
                CookieUtils.buildCookie(
                        original.getName(),
                        original.getValue(),
                        rememberMe ? rememberMeProperties.getMaxAge() : sessionCookieProperties.getMaxAge(),
                        sessionCookieProperties.getPath(),
                        sessionCookieProperties.isSecure(),
                        sessionCookieProperties.isHttpOnly(),
                        sessionCookieProperties.getSameSite()
                )
        );
    }
}
