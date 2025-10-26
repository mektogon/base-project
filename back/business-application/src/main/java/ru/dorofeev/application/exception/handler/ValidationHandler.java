package ru.dorofeev.application.exception.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.dorofeev.application.exception.model.ErrorData;
import ru.dorofeev.application.exception.model.ErrorResponse;
import ru.dorofeev.application.exception.model.enums.ErrorType;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidationHandler extends BaseHandler {

    private static final String DEFAULT_WARN_MESSAGE = "Ошибка валидации при работе приложения: ";

    @Value("${app.system}")
    private String system;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn(DEFAULT_WARN_MESSAGE, ex);
        final List<ErrorData> commonErrors = Stream.concat(
                ex.getBindingResult().getGlobalErrors().stream()
                        .map(error -> buildErrorData(EMPTY_ERROR_FIELD, error.getDefaultMessage())),
                ex.getBindingResult().getFieldErrors().stream()
                        .map(error -> buildErrorData(error.getField(), error.getDefaultMessage()))
        ).toList();

        return ErrorResponse.builder()
                .system(system)
                .code(ErrorType.VALIDATION_ERROR.getCode())
                .type(ErrorType.VALIDATION_ERROR.name())
                .validation(commonErrors)
                .build();
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onConstraintValidationException(ConstraintViolationException ex) {
        log.warn(DEFAULT_WARN_MESSAGE, ex);
        final List<ErrorData> violations = ex.getConstraintViolations()
                .stream()
                .map(violation -> buildErrorData(
                        StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                                .reduce((first, last) -> last)
                                .map(Path.Node::getName)
                                .orElse(null),
                        violation.getMessage()
                ))
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .system(system)
                .code(ErrorType.VALIDATION_ERROR.getCode())
                .type(ErrorType.VALIDATION_ERROR.name())
                .validation(violations)
                .build();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.warn(DEFAULT_WARN_MESSAGE, ex);
        return ErrorResponse.builder()
                .system(system)
                .code(ErrorType.VALIDATION_ERROR.getCode())
                .type(ErrorType.VALIDATION_ERROR.name())
                .validation(Collections.singletonList(
                        buildErrorData(
                                ex.getParameterName(),
                                ex.getMessage()
                        )
                ))
                .build();
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse onInvalidFormatException(InvalidFormatException ex) {
        log.warn(DEFAULT_WARN_MESSAGE, ex);
        Optional<JsonMappingException.Reference> pathException = ex.getPath().stream().findFirst();

        String errorFieldName = null;
        if (pathException.isPresent()) {
            errorFieldName = pathException.get().getFieldName();
        }

        return ErrorResponse.builder()
                .system(system)
                .code(ErrorType.VALIDATION_ERROR.getCode())
                .type(ErrorType.VALIDATION_ERROR.name())
                .validation(Collections.singletonList(
                        buildErrorData(
                                errorFieldName,
                                ex.getMessage()
                        )
                ))
                .build();
    }
}