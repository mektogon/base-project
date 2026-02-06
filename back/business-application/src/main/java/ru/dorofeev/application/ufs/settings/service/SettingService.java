package ru.dorofeev.application.ufs.settings.service;

import ru.dorofeev.application.ufs.settings.model.UfsSettingsResponse;

/**
 * Контракт для взаимодействия с настройками приложения.
 */
public interface SettingService {

    /**
     * Метод получения настроек приложения.
     *
     * @return объект типа {@link UfsSettingsResponse}.
     */
    UfsSettingsResponse getSettings();
}
