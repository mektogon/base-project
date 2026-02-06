package ru.dorofeev.application.ufs.authentication.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.dorofeev.application.validator.annotation.PasswordValidation;
import ru.dorofeev.security.introspector.base64decode.Base64StringDecode;

@Schema(description = "Модель запроса с параметрами сброса пароля")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UfsChangePasswordRequest extends UfsLoginRequest {

    @NotBlank(message = "Новый пароль не может быть пустым!")
    @Schema(
            description = "Новый пароль пользователя",
            format = "base64",
            example = "ZXhhbXBsZQ=="
    )
    @Base64StringDecode
    @PasswordValidation
    private String newPassword;

    @JsonIgnore
    @Override
    public void setRememberMe(boolean rememberMe) {
        //Не используется в рамках данного сценария.
        super.setRememberMe(rememberMe);
    }
}
