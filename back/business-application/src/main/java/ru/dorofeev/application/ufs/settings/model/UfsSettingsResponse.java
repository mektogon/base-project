package ru.dorofeev.application.ufs.settings.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Schema(description = "Модель ответа с настройками приложения")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UfsSettingsResponse {

    @Schema(description = "Настройки приложения в пре-логине")
    private Map<String, Object> parameters;
}
