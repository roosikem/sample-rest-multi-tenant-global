package com.lkup.accounts.dto.deployment;

import com.lkup.accounts.dto.configuration.ConfigurationDto;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateDeploymentDto {
    @NotBlank(message = "Id cannot be blank")
    private String id;

    private String deploymentName;
    private Boolean status;
    private ConfigurationDto configuration;
}
