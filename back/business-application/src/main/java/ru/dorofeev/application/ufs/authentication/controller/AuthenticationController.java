package ru.dorofeev.application.ufs.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dorofeev.application.exception.model.ErrorResponse;
import ru.dorofeev.application.ufs.authentication.model.request.UfsChangePasswordRequest;
import ru.dorofeev.application.ufs.authentication.model.request.UfsLoginRequest;
import ru.dorofeev.application.ufs.authentication.model.request.UfsRegistrationRequest;
import ru.dorofeev.application.ufs.authentication.model.response.UfsAuthenticationResponse;
import ru.dorofeev.application.ufs.authentication.service.AuthenticationServiceProxy;
import ru.dorofeev.application.validator.group.PasswordValidationGroup;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
@Tag(name = "AuthenticationController", description = "Точка доступа к аутентификации")
public class AuthenticationController {

    private final AuthenticationServiceProxy service;

    @Operation(description = "Аутентификация пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UfsAuthenticationResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping("/login")
    public UfsAuthenticationResponse login(@Valid @RequestBody UfsLoginRequest request) {
        return service.login(request);
    }

    @Operation(description = "Выход пользователя из системы",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping("/logout")
    public void logout() {
        service.logout();
    }

    @Operation(description = "Регистрация пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UfsAuthenticationResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping("/registration")
    public UfsAuthenticationResponse registration(
            @Validated(value = PasswordValidationGroup.OnRegistration.class)
            @RequestBody UfsRegistrationRequest request
    ) {
        return service.registration(request);
    }

    @Operation(description = "Изменение пароля пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping("/password/change")
    public void changePassword(@Valid @RequestBody UfsChangePasswordRequest request) {
        service.changePassword(request);
    }

    @Operation(description = "Продление аутентификации при активном сценарии",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    @PostMapping("/extend")
    public void extend() {
        //Продление сессии происходит автоматически при вызове endpoint`a.
        //Endpoint исключительно для длительных сценариев, когда от пользователя не поступают запросы.
    }
}
