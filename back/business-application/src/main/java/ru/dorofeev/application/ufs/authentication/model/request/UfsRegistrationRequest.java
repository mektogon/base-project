package ru.dorofeev.application.ufs.authentication.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Schema(description = "Модель запроса с параметрами регистрации пользователя")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UfsRegistrationRequest extends UfsLoginRequest {

    @NotBlank(message = "Имя пользователя не может быть пустым!")
    @Schema(description = "Имя пользователя")
    private String firstName;

    @Schema(description = "Фамилия пользователя")
    private String lastName;

    @Schema(description = "Отчество пользователя")
    private String middleName;

    @Pattern(regexp = "^\\+7\\d{10}$", message = "Номер телефона должен соответствовать стандарту страны!")
    @Schema(description = "Телефон пользователя", example = "+77777777777")
    private String phoneNumber;

    @JsonIgnore
    @Override
    public void setRememberMe(boolean rememberMe) {
        //Не используется в рамках данного сценария.
        super.setRememberMe(rememberMe);
    }
}
