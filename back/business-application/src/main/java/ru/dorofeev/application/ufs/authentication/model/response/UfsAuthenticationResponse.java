package ru.dorofeev.application.ufs.authentication.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Schema(description = "Модель ответа аутентифицированного пользователя")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UfsAuthenticationResponse {

    @Schema(description = "Имя пользователя")
    private String firstName;

    @Schema(description = "Отчество пользователя")
    private String middleName;
}
