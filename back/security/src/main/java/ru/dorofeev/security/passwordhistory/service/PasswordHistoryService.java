package ru.dorofeev.security.passwordhistory.service;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Контракт для взаимодействия с историей паролей.
 */
public interface PasswordHistoryService {

    /**
     * Сохранение пароля в историю.
     *
     * @param userId          идентификатор пользователя.
     * @param encodedPassword текущий пароль пользователя. (Хешированный)
     */
    void savePasswordHash(@NotNull UUID userId, @NotNull String encodedPassword);

    /**
     * Проверка текущего пароля на соответствие одному из предыдущих паролей пользователя.
     *
     * @param userId      идентификатор пользователя.
     * @param rawPassword текущий пароль пользователя. (Не хешированный)
     * @return true/false в зависимости от условий.
     */
    boolean checkRawPassword(@NotNull UUID userId, @NotNull String rawPassword);
}
