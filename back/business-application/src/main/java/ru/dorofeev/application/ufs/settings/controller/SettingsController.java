package ru.dorofeev.application.ufs.settings.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dorofeev.application.exception.model.ErrorResponse;
import ru.dorofeev.application.ufs.settings.model.UfsSettingsResponse;
import ru.dorofeev.application.ufs.settings.service.SettingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settings")
@Tag(name = "SettingsController", description = "Точка доступа к настройкам приложения")
public class SettingsController {

    private final SettingService service;

    @Operation(description = "Получение настроек приложения",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UfsSettingsResponse.class))
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
    @GetMapping(value = "/pre-login")
    public UfsSettingsResponse getPreLoginSettings() {
        return service.getSettings();
    }
}
