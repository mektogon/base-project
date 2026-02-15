package ru.dorofeev.security.session.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.dorofeev.security.session.enums.UserChannel;

import java.util.Objects;

@UtilityClass
public class SessionUtils {

    private static final String MOBILE_PATTERN_VALUE = "(?i).*(mobile|android|iphone|ipod|ipad|blackberry|webos|windows phone|opera mini|tablet|kindle|silk).*";
    private static final String USER_AGENT_VALUE = "User-Agent";

    /**
     * Метод получения канала пользователя по заголовку "User-Agent" HTTP-запроса.
     *
     * @param request http-запрос.
     * @return WEB\MOBILE.
     */
    public static @NotNull UserChannel getChannelByUserAgent(HttpServletRequest request) {

        if (Objects.isNull(request)) {
            return UserChannel.WEB;
        }

        String currentUserAgent = request.getHeader(USER_AGENT_VALUE);
        if (StringUtils.isBlank(currentUserAgent)) {
            return UserChannel.WEB;
        }

        return currentUserAgent.matches(MOBILE_PATTERN_VALUE) ? UserChannel.MOBILE : UserChannel.WEB;
    }

    /**
     * Метод получения IP-адреса пользователя.
     *
     * @param request http-запрос.
     * @return IP-адреса пользователя.
     */
    public static @Nullable String getIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}
