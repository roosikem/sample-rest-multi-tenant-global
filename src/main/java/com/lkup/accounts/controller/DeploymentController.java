package com.lkup.accounts.controller;

import com.lkup.accounts.document.Deployment;
import com.lkup.accounts.dto.deployment.CreateDeploymentDto;
import com.lkup.accounts.dto.deployment.DeploymentDto;
import com.lkup.accounts.dto.deployment.UpdateDeploymentDto;
import com.lkup.accounts.mapper.DeploymentMapper;
import com.lkup.accounts.service.ConfigurationService;
import com.lkup.accounts.service.DeploymentService;
import com.lkup.accounts.utilities.PermissionConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/deployments")
public class DeploymentController {

    private final DeploymentService deploymentService;
    private final DeploymentMapper deploymentMapper;
    private final ConfigurationService configurationService;

    public DeploymentController(DeploymentService deploymentService, ConfigurationService configurationService, DeploymentMapper deploymentMapper) {
        this.deploymentService = deploymentService;
        this.deploymentMapper = deploymentMapper;
        this.configurationService = configurationService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_DEPLOYMENT + "', '" + PermissionConstants.CREATE_DEPLOYMENT + "')")
    public ResponseEntity<List<DeploymentDto>> getAllDeployments() {
        List<Deployment> deployments = deploymentService.findAllDeployments();
        return ResponseEntity.ok(deploymentMapper.convertDeploymentsToDtos(deployments));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.CREATE_DEPLOYMENT + "')")
    public ResponseEntity<DeploymentDto> createDeployment(@RequestBody @Validated CreateDeploymentDto createDeploymentdto) {
        Deployment deployment = deploymentMapper.convertCreateDtoToDeployment(createDeploymentdto);
        Deployment createDeployment = deploymentService.createDeployment(deployment);
        Optional<Deployment> publishDeployment = deploymentService.publishConfiguration(createDeployment);
        if (publishDeployment.isPresent())
            createDeployment = publishDeployment.get();
        DeploymentDto responseDeploymentDto = deploymentMapper.convertDeploymentToDto(createDeployment);
        return ResponseEntity.created(URI.create("/api/v1/deployments/" + responseDeploymentDto.getId())).body(responseDeploymentDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_DEPLOYMENT + "','" + PermissionConstants.CREATE_DEPLOYMENT + "')")
    public ResponseEntity<DeploymentDto> getDeploymentById(@PathVariable String id) {
        Optional<Deployment> deployment = deploymentService.findDeploymentById(id);
        return deployment.map(deploymentMapper::convertDeploymentToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.UPDATE_DEPLOYMENT + "')")
    public ResponseEntity<DeploymentDto> updateDeployment(@PathVariable String id, @RequestBody @Validated UpdateDeploymentDto deploymentDto) {
        deploymentDto.setId(id);
        Optional<Deployment> updatedDeployment = deploymentService.updateDeployment(deploymentMapper.convertUpdateDtoToDeployment(deploymentDto));
        if (updatedDeployment.isPresent())
            updatedDeployment = deploymentService.publishConfiguration(updatedDeployment.get());
        return updatedDeployment.map(deploymentMapper::convertDeploymentToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
