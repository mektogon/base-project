package ru.dorofeev.application.exception.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.dorofeev.application.exception.BaseProjectException;
import ru.dorofeev.application.exception.model.ErrorResponse;
import ru.dorofeev.application.exception.model.enums.ErrorType;

@RestControllerAdvice
public class RestExceptionHandler {

    @Value("${app.system}")
    private String system;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse handleException() {
        return ErrorResponse.builder()
                .system(system)
                .code(ErrorType.OTHER_EXCEPTION.getCode())
                .type(ErrorType.OTHER_EXCEPTION.name())
                .build();
    }

    @ExceptionHandler(BaseProjectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse handleException(BaseProjectException ex) {
        return ErrorResponse.builder()
                .system(system)
                .code(String.valueOf(ex.getErrorType().getCode()))
                .type(ex.getErrorType().name())
                .build();
    }
}