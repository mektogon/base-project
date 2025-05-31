package ru.dorofeev.application.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.dorofeev.application.exception.BaseProjectException;
import ru.dorofeev.application.exception.model.ErrorResponse;
import ru.dorofeev.application.exception.model.enums.ErrorType;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    private static final String DEFAULT_ERROR_MESSAGE = "Непредвиденная ошибка при работе приложения: ";

    @Value("${app.system}")
    private String system;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse handleException(Exception ex) {
        log.error(DEFAULT_ERROR_MESSAGE, ex);
        return ErrorResponse.builder()
                .system(system)
                .code(ErrorType.OTHER_EXCEPTION.getCode())
                .type(ErrorType.OTHER_EXCEPTION.name())
                .build();
    }

    @ExceptionHandler(BaseProjectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse handleException(BaseProjectException ex) {
        log.error(DEFAULT_ERROR_MESSAGE, ex);
        return ErrorResponse.builder()
                .system(system)
                .code(String.valueOf(ex.getErrorType().getCode()))
                .type(ex.getErrorType().name())
                .build();
    }
}