package ru.dorofeev.application.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.dorofeev.application.exception.BaseProjectException;
import ru.dorofeev.application.exception.model.ErrorResponse;
import ru.dorofeev.application.exception.model.enums.ErrorType;

import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends BaseHandler {

    private static final String DEFAULT_ERROR_MESSAGE = "Непредвиденная ошибка при работе приложения: ";
    private static final String MESSAGE_METHOD_NOT_ALLOWED = "Обращение к API: '%s' невозможно по HTTP-method: '%s'!";
    private static final String MESSAGE_NOT_FOUND = "Не удалось найти API: '%s'!";

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

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoResourceFoundException(NoResourceFoundException ex) {
        return ErrorResponse.builder()
                .system(system)
                .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .type(HttpStatus.NOT_FOUND.name())
                .validation(Collections.singletonList(
                        buildErrorData(
                                EMPTY_ERROR_FIELD,
                                String.format(MESSAGE_NOT_FOUND, ex.getResourcePath())
                        )
                ))
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex,
            WebRequest request
    ) {
        return ErrorResponse.builder()
                .system(system)
                .code(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()))
                .type(HttpStatus.METHOD_NOT_ALLOWED.name())
                .validation(Collections.singletonList(
                        buildErrorData(
                                EMPTY_ERROR_FIELD,
                                String.format(
                                        MESSAGE_METHOD_NOT_ALLOWED,
                                        ((ServletWebRequest) request).getRequest().getRequestURI(),
                                        ex.getMethod()
                                )
                        )
                ))
                .build();
    }
}