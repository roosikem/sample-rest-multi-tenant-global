package com.lkup.accounts.dto.configuration;

import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.dto.team.TeamDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter
@Setter
public class ConfigurationDto {
    private String id;
    private String name;
    private String description;


    private OrganizationDto organization;

    private TeamDto team;

    private String widgetColor;

    private String alignment;

    private String sideSpace;

    private String bottomSpace;

    private Integer launcherButtonVisibility;

    private Object widgetConfig;

    private String configUrl;

    private String environment;

    private String appId;

    private String hostUrl;

    private String authTokenUrl;

    private String language;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
