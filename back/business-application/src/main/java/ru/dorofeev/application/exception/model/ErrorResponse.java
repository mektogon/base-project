package ru.dorofeev.application.exception.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Schema(description = "Модель ошибки")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @Schema(description = "Наименование системы")
    private String system;

    @Schema(description = "Код ошибки")
    private String code;

    @Schema(description = "Тип ошибки")
    private String type;

    @Schema(description = "Ошибки валидации")
    private List<ErrorData> validation;
}
