package ru.dorofeev.security.cookie.utils;

import jakarta.servlet.http.Cookie;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

@UtilityClass
public final class CookieUtils {

    /**
     * Метод формирования {@link Cookie}.
     *
     * @param name     наименование cookie.
     * @param value    значение cookie
     * @param maxAge   длительность жизни cookie.
     * @param path     путь присваивания cookie.
     * @param secure   передача cookie по HTTPS.
     * @param httpOnly доступ к cookie из JavaScript на клиентской стороне.
     * @param sameSite передача cookie с cross-site запросами. (DEFAULT: "Strict")
     * @return объект типа {@link Cookie}.
     */
    public Cookie buildCookie(
            @NotNull String name,
            @NotNull String value,
            @NotNull Duration maxAge,
            @Nullable String path,
            boolean secure,
            boolean httpOnly,
            @Nullable String sameSite
    ) {
        final Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(Math.toIntExact(maxAge.getSeconds()));
        cookie.setPath(StringUtils.isBlank(path) ? "/" : path);
        cookie.setHttpOnly(httpOnly);
        cookie.setSecure(secure);

        final String sameSiteAttributeName = "SameSite";
        if (StringUtils.isNotBlank(sameSite)) {
            cookie.setAttribute(sameSiteAttributeName, sameSite);
        } else {
            cookie.setAttribute(sameSiteAttributeName, "Strict");
        }

        return cookie;
    }
}
