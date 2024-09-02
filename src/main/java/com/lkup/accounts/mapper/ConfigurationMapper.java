package com.lkup.accounts.mapper;

import com.lkup.accounts.document.*;
import com.lkup.accounts.dto.configuration.ConfigurationDto;
import com.lkup.accounts.dto.configuration.CreateConfigurationDto;
import com.lkup.accounts.dto.configuration.UpdateConfigurationDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConfigurationMapper {

    private final ModelMapper modelMapper;

    public ConfigurationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ConfigurationDto convertConfigurationToDto(Configuration configuration) {
        return modelMapper.map(configuration, ConfigurationDto.class);
    }

    public List<ConfigurationDto> convertConfigurationsToDtos(List<Configuration> configurations) {
        return configurations.stream().map(this::convertConfigurationToDto).collect(Collectors.toList());
    }

    public Configuration convertCreateDtoToConfiguration(CreateConfigurationDto createConfigurationDto) {
        Configuration configuration = modelMapper.map(createConfigurationDto, Configuration.class);
        configuration.setOrganizationId(createConfigurationDto.getOrganization());
        configuration.setTeamId(createConfigurationDto.getTeam());
        configuration.setAppId(AppId.builder().id(createConfigurationDto.getAppId()).build());
        configuration.setEnvironment(Environment.builder().id(createConfigurationDto.getEnvironment()).build());
        return configuration;
    }

    public Configuration convertUpdateDtoToConfiguration(UpdateConfigurationDto updateConfigurationDto) {
        Configuration configuration = modelMapper.map(updateConfigurationDto, Configuration.class);
        configuration.setOrganizationId(updateConfigurationDto.getOrganization());
        configuration.setTeamId(updateConfigurationDto.getTeam());
        configuration.setAppId(AppId.builder().id(updateConfigurationDto.getAppId()).build());
        configuration.setEnvironment(Environment.builder().id(updateConfigurationDto.getEnvironment()).build());
        return configuration;
    }
}
