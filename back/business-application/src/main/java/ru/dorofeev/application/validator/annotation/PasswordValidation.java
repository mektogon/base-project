package ru.dorofeev.application.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.dorofeev.application.validator.PasswordValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для валидации пароля пользователя.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordValidation {

    String message() default "WARNING! Значение формируется динамически!";

    /**
     * @return минимальная длина пароля.
     */
    int min() default 12;

    /**
     * @return максимальная длина пароля.
     */
    int max() default Integer.MAX_VALUE;

    /**
     * @return группа валидации.
     */
    Class<?>[] groups() default {};

    /**
     * @return дополнительная метаинформация о нарушении ограничения.
     */
    Class<? extends Payload>[] payload() default {};
}
