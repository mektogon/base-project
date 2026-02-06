package ru.dorofeev.security.cookie.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.BooleanUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import ru.dorofeev.SecurityProperties;
import ru.dorofeev.security.authentication.utils.AuthenticationUtils;
import ru.dorofeev.security.cookie.strategy.CookieRefreshStrategy;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Фильтр обновления параметра "MaxAge" {@link Cookie}.
 */
public final class CookieRefreshFilter extends OncePerRequestFilter {

    private final List<CookieRefreshStrategy> refreshStrategies;

    private final List<String> whiteListNotAuthentification;
    private final List<String> cookieCandidatesToRefresh;
    private final String rememberMeAttributeName;
    private final AntPathMatcher pathMatcher;

    public CookieRefreshFilter(
            List<CookieRefreshStrategy> refreshStrategies,
            SecurityProperties securityProperties
    ) {
        this.refreshStrategies = refreshStrategies;
        this.pathMatcher = new AntPathMatcher();

        this.cookieCandidatesToRefresh = securityProperties.getCookie().getCookieRefreshFilter().getCandidateToRefresh();
        this.whiteListNotAuthentification = securityProperties.getEndpoint().getWhiteList();
        this.rememberMeAttributeName = securityProperties.getSession().getRememberMe().getAttributeName();
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (support(request, response)) {
            handle(request, response);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String contextPath = request.getServletPath();
        return whiteListNotAuthentification
                .stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, contextPath));
    }

    /**
     * Обработка текущего запроса с {@link Cookie#setMaxAge(int)}.
     *
     * @param request  текущий HTTP-запрос.
     * @param response текущий HTTP-ответ.
     */
    private void handle(HttpServletRequest request, HttpServletResponse response) {
        boolean rememberMe = BooleanUtils.isTrue((Boolean) getCurrentHttpSession(request)
                .getAttribute(rememberMeAttributeName));

        cookieCandidatesToRefresh.forEach(cookieName -> {
            Cookie currentCookie = WebUtils.getCookie(request, cookieName);
            if (Objects.nonNull(currentCookie)) {
                refreshStrategies
                        .stream()
                        .filter(strategy -> strategy.support(currentCookie.getName()))
                        .findFirst()
                        .ifPresent(strategy -> strategy.apply(currentCookie, response, rememberMe));
            }
        });
    }

    /**
     * Проверка возможности обновления {@link Cookie}.
     *
     * @param request  текущий HTTP-запрос.
     * @param response текущий HTTP-ответ.
     * @return true - обновление применимо; false - обновление не применимо.
     */
    private boolean support(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response) {

        if (response.isCommitted()) {
            return false;
        }

        if (Objects.isNull(getCurrentHttpSession(request))) {
            return false;
        }

        return AuthenticationUtils.isAuthenticated();
    }

    /**
     * Получение текущий HTTP-сессии.
     *
     * @param request текущий HTTP-запрос.
     * @return объект типа {@link HttpSession}.
     */
    private static HttpSession getCurrentHttpSession(HttpServletRequest request) {
        return request.getSession(false);
    }
}
