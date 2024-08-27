package com.lkup.accounts.mapper;

import com.lkup.accounts.document.Environment;
import com.lkup.accounts.document.Team;
import com.lkup.accounts.dto.environment.CreateEnvironmentDto;
import com.lkup.accounts.dto.environment.EnvironmentDto;
import com.lkup.accounts.dto.environment.UpdateEnvironmentDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnvironmentMapper {

    private ModelMapper modelMapper;
    private final APIKeyMapper apiKeyMapper;
    private final APPIdMapper appIdMapper;
    private final OrganizationMapper organizationMapper;

    public EnvironmentMapper(ModelMapper modelMapper, APIKeyMapper apiKeyMapper, APPIdMapper appIdMapper, OrganizationMapper organizationMapper) {
        this.apiKeyMapper = apiKeyMapper;
        this.appIdMapper = appIdMapper;
        this.organizationMapper = organizationMapper;
        this.modelMapper = modelMapper;
    }

    public EnvironmentDto convertEnvironmentToDto(Environment environment) {
        if (environment == null) {
            return null;
        }
        EnvironmentDto environmentDto = modelMapper.map(environment, EnvironmentDto.class);

        if (environment.getId() != null) {
            environmentDto.setId(environment.getId());
        }
        if (environment.getName() != null) {
            environmentDto.setName(environment.getName());
        }
        environmentDto.setApiKeys(apiKeyMapper.convertAPIKeysToDtos(environment.getApiKeys()));
        environmentDto.setAppIds(appIdMapper.convertAPPIdsToDtos(environment.getAppIds()));
        if (null != environment.getOrganization()) {
            environmentDto.setOrganizationDto(organizationMapper.convertOrganizationToDto(environment.getOrganization()));
        }
        return environmentDto;
    }

    public Environment convertCreateDtoToEnvironment(CreateEnvironmentDto createEnvironmentDto) {
        if (createEnvironmentDto == null) {
            return null;
        }
        Environment environment = modelMapper.map(createEnvironmentDto, Environment.class);
        if (createEnvironmentDto.getName() != null) {
            environment.setName(createEnvironmentDto.getName());
        }
        if (createEnvironmentDto.getAppIds() != null) {
            environment.setAppIds(appIdMapper.convertIdsToAppId(createEnvironmentDto.getAppIds()));
        }
        if (null != createEnvironmentDto.getOrganizationId()) {
            environment.setOrganization(organizationMapper.convertOrgIdToOrganization(createEnvironmentDto.getOrganizationId()));
        }
        if (null != createEnvironmentDto.getTeamId()) {
            Team team = new Team();
            team.setId(createEnvironmentDto.getTeamId());
            environment.setTeam(team);
        }
        return environment;
    }

    public UpdateEnvironmentDto convertEnvironmentToUpdateDto(Environment environment) {
        if (environment == null) {
            return null;
        }
        UpdateEnvironmentDto updateEnvironmentDto = modelMapper.map(environment, UpdateEnvironmentDto.class);
        if (environment.getId() != null) {
            updateEnvironmentDto.setId(environment.getId());
        }
        if (environment.getName() != null) {
            updateEnvironmentDto.setName(environment.getName());
        }
        updateEnvironmentDto.setApiKeys(apiKeyMapper.extractIdsFromAPIKeys(environment.getApiKeys()));
        return updateEnvironmentDto;
    }

    public Environment convertUpdateDtoToEnvironment(UpdateEnvironmentDto updateEnvironmentDto) {
        if (updateEnvironmentDto == null) {
            return null;
        }
        Environment environment = modelMapper.map(updateEnvironmentDto, Environment.class);
        if (updateEnvironmentDto.getId() != null) {
            environment.setId(updateEnvironmentDto.getId());
        }
        if (updateEnvironmentDto.getName() != null) {
            environment.setName(updateEnvironmentDto.getName());
        }
        environment.setApiKeys(apiKeyMapper.convertIdsToAPIKeys(updateEnvironmentDto.getApiKeys()));

        if (updateEnvironmentDto.getAppIds() != null) {
            environment.setAppIds(appIdMapper.convertIdsToAppId(updateEnvironmentDto.getAppIds()));
        }
        if (null != updateEnvironmentDto.getOrganizationId()) {
            environment.setOrganization(organizationMapper.convertOrgIdToOrganization(updateEnvironmentDto.getOrganizationId()));
        }
        return environment;
    }

    public CreateEnvironmentDto convertEnvironmentToCreateDto(Environment environment) {
        if (environment == null) {
            return null;
        }
        CreateEnvironmentDto createEnvironmentDto = new CreateEnvironmentDto();
        if (environment.getName() != null) {
            createEnvironmentDto.setName(environment.getName());
        }
        return createEnvironmentDto;
    }

    public List<EnvironmentDto> convertEnvironmentsToDtos(Iterable<Environment> environments) {
        List<EnvironmentDto> environmentDtos = new ArrayList<>();
        if (environments != null) {
            for (Environment environment : environments) {
                environmentDtos.add(convertEnvironmentToDto(environment));
            }
        }
        return environmentDtos;
    }

    public Environment convertEnvironmentDtoToEnvironment(EnvironmentDto environmentDto) {
        if (environmentDto == null) {
            return null;
        }

        Environment environment = new Environment();
        if (environmentDto.getName() != null) {
            environment.setName(environmentDto.getName());
        }
        if (environmentDto.getId() != null) {
            environment.setId(environmentDto.getId());
        }

        if (environmentDto.getAppIds() != null) {
            environment.setAppIds(appIdMapper.convertAPPIdsDtoToAppId(environmentDto.getAppIds()));
        }
        return environment;
    }

    public List<Environment> convertEnvironmentsDtoToEnvironments(List<EnvironmentDto> environments) {
        List<Environment> environmentsDocuments = new ArrayList<>();
        if (environments != null) {
            for (EnvironmentDto environment : environments) {
                environmentsDocuments.add(convertEnvironmentDtoToEnvironment(environment));
            }
        }
        return environmentsDocuments;
    }
}
