package ru.dorofeev.security.session.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.stereotype.Component;
import ru.dorofeev.security.utils.LoggerUtils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
@Component
public class SessionEventListener {

    @EventListener
    public void processSessionCreatedEvent(SessionCreatedEvent event) {
        LoggerUtils.debugLazy(
                () ->
                        """
                                \n
                                |===============================================|
                                [SESSION-CREATED] Создание сессии c ID: '%s';
                                Время создания: '%s';
                                Timeout: '%s';
                                |===============================================|
                                """.formatted(
                                event.getSessionId(),
                                toOffsetDateTime(event.getTimestamp()),
                                event.getSession().getMaxInactiveInterval().toSeconds()
                        )
        );
    }

    @EventListener
    public void processSessionDestroyedEvent(SessionDestroyedEvent event) {
        LoggerUtils.debugLazy(
                () ->
                        """
                                \n
                                |===============================================|
                                [SESSION-CREATED] Уничтожение сессии c ID: '%s';
                                Время уничтожения: '%s';
                                |===============================================|
                                """.formatted(
                                event.getSessionId(),
                                toOffsetDateTime(event.getTimestamp())
                        )
        );
    }

    @EventListener
    public void processSessionExpiredEvent(SessionExpiredEvent event) {
        LoggerUtils.debugLazy(
                () ->
                        """
                                \n
                                |===============================================|
                                [SESSION-CREATED] Истечение сессии c ID: '%s';
                                Время истечения: '%s';
                                |===============================================|
                                """.formatted(
                                event.getSessionId(),
                                toOffsetDateTime(event.getTimestamp())
                        )
        );
    }

    /**
     * Преобразование временной метки типа {@link Long} в {@link OffsetDateTime} {@link ZoneOffset#UTC}.
     *
     * @param timestamp числовое представление временной метки.
     * @return объект типа {@link OffsetDateTime}.
     */
    private OffsetDateTime toOffsetDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atOffset(ZoneOffset.UTC);
    }
}
