package ru.dorofeev.application.exception.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "Тип ошибки")
public enum ErrorType {

    /**
     * Неизвестный тип ошибки.
     */
    OTHER_EXCEPTION("BPB1000"),

    /**
     * Ошибка любой интеграции.
     */
    INTEGRATION_ERROR("BPB2000"),

    /**
     * Ошибка валидации.
     */
    VALIDATION_ERROR("BPB3000");

    @Schema(description = "Код ошибки")
    private final String code;
}
