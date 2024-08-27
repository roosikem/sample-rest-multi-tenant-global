package com.lkup.accounts.dto.configuration;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateConfigurationDto {
    private String id;

    @NotNull
    @NotBlank(message = "Name cannot be blank")
    @Schema(name = "Configuration Name", example = "dev config", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(name = "Configuration description")
    private String description;

    @Schema(name = "Organization Id", example = "12344", requiredMode = Schema.RequiredMode.REQUIRED)
    private String organization;

    @Schema(name = "Team Id", example = "12344", requiredMode = Schema.RequiredMode.REQUIRED)
    private String team;

    private String widgetColor;

    private String alignment;

    private String sideSpace;

    private String bottomSpace;

    private Integer launcherButtonVisibility;

    @Schema(name = "Widget Configuration", example = "12344")
    private Object widgetConfig;

    @NotBlank(message = "environment cannot be blank")
    @Schema(name = "Environment Id", example = "12344")
    private String environment;

    @NotBlank(message = "appId cannot be blank")
    @Schema(name = "App Id", example = "12344")
    private String appId;

    private String hostUrl;

    private String authTokenUrl;

    @NotBlank(message = "language cannot be blank")
    @Schema(name = "Language", example = "en")
    private String language;
}
