package ru.dorofeev.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.dorofeev.notification.model.enums.NotificationType;

/**
 * Контракт отправляемого уведомления.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {

    /**
     * Тип уведомления.
     */
    private NotificationType type;

    /**
     * Получатель сообщения.
     */
    private String recipient;

    /**
     * Отправляемое сообщение.
     */
    private String message;
}
