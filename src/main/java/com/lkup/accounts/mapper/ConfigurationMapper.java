package com.lkup.accounts.mapper;

import com.lkup.accounts.document.Configuration;
import com.lkup.accounts.document.Deployment;
import com.lkup.accounts.dto.configuration.ConfigurationDto;
import com.lkup.accounts.dto.configuration.CreateConfigurationDto;
import com.lkup.accounts.dto.configuration.UpdateConfigurationDto;
import com.lkup.accounts.dto.deployment.CreateDeploymentDto;
import com.lkup.accounts.dto.deployment.DeploymentDto;
import com.lkup.accounts.dto.deployment.UpdateDeploymentDto;
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
        return modelMapper.map(createConfigurationDto, Configuration.class);
    }

    public Configuration convertUpdateDtoToConfiguration(UpdateConfigurationDto updateConfigurationDto) {
        return modelMapper.map(updateConfigurationDto, Configuration.class);
    }
}
