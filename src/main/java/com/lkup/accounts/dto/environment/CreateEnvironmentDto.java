package com.lkup.accounts.dto.environment;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateEnvironmentDto {
    private String id;
    @NotNull
    @Schema(name = "Environment Name", example = "Dev", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    private List<String> apiKeys;

    @JsonProperty("appIds")
    @NotNull
    @Schema(name = "App Ids", example = "[1234,34534]", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> appIds;

    @Schema(name = "App Ids", example = "https://host.com/dev")
    private String hostUrl;

    @Schema(name = "App Ids", example = "https://auth.com/dev")
    private String authTokenUrl;

    @Schema(name = "Default Config Template", example = "{}")
    private String defaultConfigTemplate;

    @NotNull
    @Schema(name = "Environment Type", example = "DEV, QA, PROD, SANDBOX, STAGING", requiredMode = Schema.RequiredMode.REQUIRED)
    private String environmentType;

    @JsonProperty("organization")
    @NotNull
    @Schema(name = "Organization ID", example = "567899SD", requiredMode = Schema.RequiredMode.REQUIRED)
    private String organizationId;

    @JsonProperty("team")
    @NotNull
    @Schema(name = "Team ID", example = "567899SD", requiredMode = Schema.RequiredMode.REQUIRED)
    private String teamId;
}
