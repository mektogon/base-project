package ru.dorofeev.notification.kafka.listener;

import ru.dorofeev.notification.model.NotificationMessage;

import java.util.List;

public interface NotificationListener {

    /**
     * Чтение событий уведомлений.
     *
     * @param notifications список уведомлений.
     */
    void listen(List<NotificationMessage> notifications);
}
