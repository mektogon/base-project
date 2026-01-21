package ru.dorofeev.application.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.dorofeev.application.exception.model.enums.ErrorType;

@Setter
@Getter
@Builder
@RequiredArgsConstructor
public class BaseProjectException extends RuntimeException {

    /**
     * Тип ошибки.
     */
    private final ErrorType errorType;

    /**
     * Наименование параметра, по которому произошла ошибка.
     */
    private final String errorField;

    /**
     * Описание ошибки.
     */
    private final String errorDesc;
}
