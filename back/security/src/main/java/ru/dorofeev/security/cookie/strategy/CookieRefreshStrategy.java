package ru.dorofeev.security.cookie.strategy;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

/**
 * Контракт реализации стратегии обновления {@link Cookie}.
 */
public interface CookieRefreshStrategy {

    /**
     * Применима ли текущая стратегия обновления.
     *
     * @param cookieName имя обновляемой куки.
     * @return true/false в зависимости от условий.
     */
    boolean support(@NotNull String cookieName);

    /**
     * Применить обновление к {@link Cookie} из HTTP-запроса.
     *
     * @param original   обновляемая {@link Cookie}.
     * @param response   HTTP-ответ.
     * @param rememberMe признак "Запомни меня".
     */
    void apply(@NotNull Cookie original, @NotNull HttpServletResponse response, boolean rememberMe);
}
