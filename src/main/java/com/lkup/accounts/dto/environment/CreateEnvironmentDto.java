package com.lkup.accounts.dto.environment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lkup.accounts.document.AppId;
import com.lkup.accounts.dto.organization.CreateOrganizationDto;
import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.dto.apikey.APIKeyDto;
import com.lkup.accounts.dto.appId.AppIdDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class CreateEnvironmentDto {
    private String id;
    private String name;

    @JsonProperty("organization")
    @Valid
    @NotNull
	private OrganizationDto organizationDto;
    private List<APIKeyDto> apiKeys;

    @JsonProperty("appIds")
    private List<AppIdDto> appIds;

    private String hostUrl;
    private String authTokenUrl;
    private String defaultConfigTemplate;
}
