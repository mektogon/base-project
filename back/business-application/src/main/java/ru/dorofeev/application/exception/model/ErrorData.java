package ru.dorofeev.application.exception.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Schema(description = "Модель ошибки")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorData {

    @Schema(description = "Наименование системы")
    private String errorField;

    @Schema(description = "Сообщение с ошибкой, выводимое на UI")
    private String errorMessage;
}
