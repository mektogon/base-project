package ru.dorofeev.security.cookie.service;

/**
 * Контракт для взаимодействия с cookies.
 */
public interface CookieService {

    /**
     * Метод удаления обязательных cookie приложения.
     * <li/> cookie для аутентификации.
     * <li/> cookie для CSRF.
     */
    void clearAppCookies();

    /**
     * Метод создания/обновление CSRF-куки.
     */
    void upsertCsrfCookieToken();
}

