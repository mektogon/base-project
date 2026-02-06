package ru.dorofeev.security.session.service;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.session.Session;
import ru.dorofeev.security.session.section.AbstractSection;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Контракт для взаимодействия с сессией пользователя
 */
public interface SessionService {

    /**
     * Получить текущую HTTP-сессию пользователя.
     *
     * @return объект типа {@link HttpSession}.
     */
    HttpSession getCurrentHttpSession();

    /**
     * Получить текущий идентификатор сессии. <br/>
     * В текущей реализации представляет собой тип {@link java.util.UUID}.
     *
     * @return идентификатор сессии текущего клиента.
     */
    String getSessionId();

    /**
     * Получить список сессий пользователя.
     *
     * @return список объектов типа {@link Session}.
     */
    Collection<? extends Session> getAllSessions();

    /**
     * Завершить все сеансы
     */
    void removeAllSessions();

    /**
     * Завершить сессию по идентификатору.
     *
     * @param sessionIdToDelete идентификатор сессии.
     */
    void removeSession(@NotNull String sessionIdToDelete);

    /**
     * Получить наименования всех секций пользователя.
     *
     * @return список с наименованиями секций.
     */
    List<String> getAllSectionNames();

    /**
     * Получить значение атрибута секции.
     *
     * @param section       объект типа {@link AbstractSection}.
     * @param attributeName атрибут секции.
     * @return значение атрибута из сессии.
     */
    @SuppressWarnings("rawtypes")
    <T extends Enum<T>> Object getSectionAttribute(@NotNull AbstractSection section, @NotNull T attributeName);

    /**
     * Получить все атрибуты сессии.
     *
     * @return [Ключ атрибута, Значение атрибута].
     */
    Map<String, Object> getSessionAttributes();

    /**
     * Инициализации контекста сессии для текущего пользователя.
     *
     * @param authentication аутентифицированный пользователь.
     * @param rootAttributes параметры, сохраняемые в корень сессии.
     */
    void initialSessionContext(@NotNull Authentication authentication, @NotNull Map<String, Object> rootAttributes);

    /**
     * Завершить текущий сеанс пользователя.
     */
    void invalidateSession();
}
