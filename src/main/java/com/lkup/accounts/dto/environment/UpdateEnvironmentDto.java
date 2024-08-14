package com.lkup.accounts.dto.environment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lkup.accounts.dto.appId.AppIdDto;
import com.lkup.accounts.dto.organization.OrganizationDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class UpdateEnvironmentDto {
    private String id;
    private String name;
    private List<String> apiKeys;

    @JsonProperty("appIds")
    private List<AppIdDto> appIds;

    private String hostUrl;
    private String authTokenUrl;
    private String defaultConfigTemplate;

    @JsonProperty("organization")
    private OrganizationDto organizationDto;
}
