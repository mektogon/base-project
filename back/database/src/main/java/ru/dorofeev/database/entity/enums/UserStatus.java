package ru.dorofeev.database.entity.enums;

/**
 * Статус аккаунта.
 */
public enum UserStatus {

    /**
     * Пользователь активен.
     */
    ACTIVE,

    /**
     * Пользователь заблокирован.
     */
    BLOCKED,

    /**
     * Пользователь удален.
     */
    DELETED,

    /**
     * Истек срок аккаунта.
     */
    EXPIRED,

    ;
}
