package com.lkup.accounts.dto.deployment;

import com.lkup.accounts.dto.configuration.ConfigurationDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDeploymentDto {
    private String id;

    @NotBlank(message = "Deployment Name cannot be blank")
    private String deploymentName;
    private Boolean status;
    private Boolean published;

    @NotNull(message = "configuration cannot be null")
    private ConfigurationDto configuration;
}
