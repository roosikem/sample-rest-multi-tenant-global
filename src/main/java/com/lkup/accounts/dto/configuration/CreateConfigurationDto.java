package com.lkup.accounts.dto.configuration;

import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.dto.team.TeamDto;
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
    private String name;

    private String description;

    private OrganizationDto organizationDto;

    private TeamDto teamDto;

    private String widgetColor;

    private String alignment;

    private String sideSpace;

    private String bottomSpace;

    private Integer launcherButtonVisibility;

    private Object widgetConfig;

    @NotBlank(message = "environment cannot be blank")
    private String environment;

    @NotBlank(message = "market cannot be blank")
    private String market;

    @NotBlank(message = "appId cannot be blank")
    private String appId;

    private String hostUrl;

    private String authTokenUrl;

    @NotBlank(message = "language cannot be blank")
    private String language;
}
