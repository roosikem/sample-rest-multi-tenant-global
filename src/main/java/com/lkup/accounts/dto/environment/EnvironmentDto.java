package com.lkup.accounts.dto.environment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lkup.accounts.dto.apikey.APIKeyDto;
import com.lkup.accounts.dto.appId.AppIdDto;
import com.lkup.accounts.dto.organization.OrganizationDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EnvironmentDto {
    private String id;
    private String name;
    private List<APIKeyDto> apiKeys;
    private List<AppIdDto> appIds;
    private String hostUrl;
    private String authTokenUrl;
    private String defaultConfigTemplate;
    private String environmentType;
    @JsonBackReference
    private OrganizationDto organizationDto;
}
