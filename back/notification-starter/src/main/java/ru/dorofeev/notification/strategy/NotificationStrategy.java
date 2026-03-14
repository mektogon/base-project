package ru.dorofeev.notification.strategy;

import ru.dorofeev.notification.model.enums.NotificationType;

/**
 * Контракт стратегии уведомлений.
 */
public interface NotificationStrategy {

    /**
     * Применима ли текущая стратегия уведомления.
     *
     * @param type тип уведомления.
     * @return true/false в зависимости от условий.
     */
    boolean support(NotificationType type);

    /**
     * Применение стратегии уведомления.
     */
    void apply();
}
