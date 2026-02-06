package ru.dorofeev.application.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import ru.dorofeev.application.validator.annotation.PasswordValidation;

/**
 * Обработчик аннотации {@link PasswordValidation}.
 */
public class PasswordValidator extends ru.dorofeev.security.utils.PasswordValidator implements ConstraintValidator<PasswordValidation, String> {

    @Value("${app.regexp.password}")
    private String regExpAllowedSymbols;

    /**
     * Минимальная длина пароля.
     */
    private int min;

    /**
     * Максимальная длина пароля.
     */
    private int max;

    @Override
    public void initialize(PasswordValidation constraintAnnotation) {
        max = constraintAnnotation.max();
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        Pair<Boolean, String> booleanStringPair = passwordIsValid(
                password,
                min,
                max,
                regExpAllowedSymbols
        );

        Boolean isValid = booleanStringPair.getKey();
        if (!isValid) {
            BuilderMessageValidator.buildMessage(context, booleanStringPair.getValue(), null);
        }

        return isValid;
    }
}
