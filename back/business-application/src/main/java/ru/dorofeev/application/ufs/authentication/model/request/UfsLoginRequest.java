package ru.dorofeev.application.ufs.authentication.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.dorofeev.application.validator.annotation.PasswordValidation;
import ru.dorofeev.application.validator.group.PasswordValidationGroup;
import ru.dorofeev.security.introspector.base64decode.Base64StringDecode;

@Schema(description = "Модель запроса с параметрами аутентификации")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UfsLoginRequest {

    @Pattern(
            message = "E-mail пользователя должен соответствовать стандарту!",
            regexp = "^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$"
    )
    @NotNull(message = "E-mail пользователя не может быть равен null!")
    @Schema(description = "Логин пользователя (E-mail)", example = "SomeEmail@mail.ru")
    private String login;

    @NotBlank(message = "Пароль пользователя не может быть пустым!")
    @Schema(
            description = "Пароль пользователя",
            format = "base64",
            example = "ZXhhbXBsZQ==",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    @Null(message = "При сбросе пароль должен быть пустым!", groups = PasswordValidationGroup.OnReset.class)
    @PasswordValidation(groups = PasswordValidationGroup.OnRegistration.class)
    @Base64StringDecode
    private String password;

    @Schema(description = "Признак 'Запомнить меня'")
    private boolean rememberMe;
}
