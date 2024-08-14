package com.lkup.accounts.dto.configuration;

import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.dto.team.TeamDto;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class UpdateConfigurationDto {
    @NotBlank(message = "id cannot be blank")
    private String id;

    @NotBlank(message = "name cannot be blank")
    private String name;
    private String description;

    private OrganizationDto organizationDto;

    private TeamDto teamDto;

    private String widgetColor;

    private String alignment;

    private String sideSpace;

    private String bottomSpace;

    private Integer launcherButtonVisibility;

    private String status;

    private Object widgetConfig;

    private String configUrl;

    private String environment;

    @NotBlank(message = "market cannot be blank")
    private String market;

    @NotBlank(message = "AppId cannot be blank")
    private String appId;

    private String hostUrl;

    private String authTokenUrl;

    @NotBlank(message = "Language cannot be blank")
    private String language;
}
