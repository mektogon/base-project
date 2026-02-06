package ru.dorofeev.application.ufs.settings.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.dorofeev.application.ufs.settings.model.UfsSettingsResponse;
import ru.dorofeev.security.cookie.service.CookieService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    private static final String REGEXP_KEY = "regexp";

    private final Map<String, String> appRegExp;

    private final CookieService cookieService;

    @Override
    public UfsSettingsResponse getSettings() {
        //Добавляем CSRF-куку, т.к. это "инициализирующий" вызов.
        cookieService.upsertCsrfCookieToken();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put(REGEXP_KEY, appRegExp);

        return UfsSettingsResponse.builder()
                .parameters(parameters)
                .build();
    }
}
