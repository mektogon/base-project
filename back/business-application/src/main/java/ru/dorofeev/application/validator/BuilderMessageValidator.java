package ru.dorofeev.application.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BuilderMessageValidator {

    /**
     * Метод формирования пользовательского сообщения валидации. <br/>
     * Метод отключает базовое сообщение, которое поставляется из аннотации.
     * После чего подкладывается пользовательское сообщение.
     *
     * @param constraintContext контекст аннотации: {@link ConstraintValidatorContext}.
     * @param message           выводимое сообщение.
     * @param errorFieldName    наименование поле, которому принадлежит ошибка.
     */
    public static void buildMessage(
            @NotNull ConstraintValidatorContext constraintContext,
            @NotNull String message,
            @Nullable String errorFieldName
    ) {
        constraintContext.disableDefaultConstraintViolation();
        constraintContext.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(errorFieldName)
                .addConstraintViolation();
    }
}
