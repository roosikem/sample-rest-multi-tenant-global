package com.lkup.accounts.mapper;

import com.lkup.accounts.document.Deployment;
import com.lkup.accounts.dto.deployment.CreateDeploymentDto;
import com.lkup.accounts.dto.deployment.DeploymentDto;
import com.lkup.accounts.dto.deployment.UpdateDeploymentDto;
import com.lkup.accounts.repository.tenant.ConfigurationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DeploymentMapper {

    private final ModelMapper modelMapper;
    private final ConfigurationRepository configurationRepository;
    private final ConfigurationMapper configurationMapper;

    public DeploymentMapper(ConfigurationRepository configurationRepository, ModelMapper modelMapper, ConfigurationMapper configurationMapper) {
        this.modelMapper = modelMapper;
        this.configurationRepository = configurationRepository;
        this.configurationMapper = configurationMapper;
    }

    public DeploymentDto convertDeploymentToDto(Deployment deployment) {
        DeploymentDto deploymentDto = modelMapper.map(deployment, DeploymentDto.class);
        if(null != deployment.getConfiguration())
            deploymentDto.setConfiguration(configurationMapper.convertConfigurationToDto(deployment.getConfiguration()));
        return deploymentDto;
    }

    public List<DeploymentDto> convertDeploymentsToDtos(List<Deployment> deployments) {
        return deployments.stream().map(this::convertDeploymentToDto).collect(Collectors.toList());
    }

    public Deployment convertCreateDtoToDeployment(CreateDeploymentDto createDeploymentdto) {
        if(null == createDeploymentdto)
            return null;
        Deployment deployment = modelMapper.map(createDeploymentdto, Deployment.class);
        if(null != createDeploymentdto.getConfiguration())
            deployment.setConfiguration(configurationRepository.findConfigurationById(createDeploymentdto.getConfiguration().getId()).orElse(null));

        return deployment;
    }

    public Deployment convertUpdateDtoToDeployment(UpdateDeploymentDto deploymentDto) {
        return modelMapper.map(deploymentDto, Deployment.class);
    }
}
