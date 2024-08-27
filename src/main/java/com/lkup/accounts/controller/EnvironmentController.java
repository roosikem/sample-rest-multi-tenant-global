package com.lkup.accounts.controller;

import com.lkup.accounts.document.Environment;
import com.lkup.accounts.dto.environment.CreateEnvironmentDto;
import com.lkup.accounts.dto.environment.EnvironmentDto;
import com.lkup.accounts.dto.environment.UpdateEnvironmentDto;
import com.lkup.accounts.mapper.EnvironmentMapper;
import com.lkup.accounts.service.EnvironmentService;
import com.lkup.accounts.utilities.PermissionConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/environments")
public class EnvironmentController {

    private final EnvironmentService environmentService;
    private final EnvironmentMapper environmentMapper;

    public EnvironmentController(EnvironmentService environmentService, EnvironmentMapper environmentMapper) {
        this.environmentService = environmentService;
        this.environmentMapper = environmentMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.CREATE_ENVIRONMENT + "')")
    public ResponseEntity<EnvironmentDto> createEnvironment(@RequestBody @Validated CreateEnvironmentDto createEnvironmentDto) {
        Environment environment = environmentMapper.convertCreateDtoToEnvironment(createEnvironmentDto);
        Environment createdEnvironment = environmentService.createEnvironment(environment);
        EnvironmentDto environmentDto = environmentMapper.convertEnvironmentToDto(createdEnvironment);
        return ResponseEntity.created(URI.create("/api/v1/environments/" + environmentDto.getId())).body(environmentDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_ENVIRONMENT + "', '" + PermissionConstants.CREATE_ENVIRONMENT + "')")
    public ResponseEntity<EnvironmentDto> getEnvironmentById(@PathVariable String id) {
        Optional<Environment> environment = environmentService.findEnvironmentById(id);
        return environment.map(environmentMapper::convertEnvironmentToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_ENVIRONMENT + "', '" + PermissionConstants.CREATE_ENVIRONMENT + "')")
    public ResponseEntity<Iterable<EnvironmentDto>> getAllEnvironments() {
        List<Environment> environments = environmentService.findAllEnvironments();
        return ResponseEntity.ok(environmentMapper.convertEnvironmentsToDtos(environments));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.UPDATE_ENVIRONMENT + "', '" + PermissionConstants.CREATE_ENVIRONMENT + "')")
    public ResponseEntity<EnvironmentDto> updateEnvironment(@PathVariable String id, @RequestBody UpdateEnvironmentDto updateEnvironmentDto) {
        updateEnvironmentDto.setId(id);
        Optional<Environment> updatedEnvironment = environmentService.updateEnvironment(environmentMapper.convertUpdateDtoToEnvironment(updateEnvironmentDto));
        return updatedEnvironment.map(environmentMapper::convertEnvironmentToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.DELETE_ENVIRONMENT + "')")
    public ResponseEntity<Void> deleteEnvironment(@PathVariable String id) {
        environmentService.deleteEnvironment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_ENVIRONMENT + "', '" + PermissionConstants.CREATE_ENVIRONMENT + "')")
    public ResponseEntity<Long> getTotalEnvironments() {
        return ResponseEntity.ok(environmentService.getTotalEnvironments());
    }

    @GetMapping("/{teamId}/total")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_ENVIRONMENT + "', '" + PermissionConstants.CREATE_ENVIRONMENT + "')")
    public ResponseEntity<Long> getTotalEnvironmentsByTeam(@PathVariable("teamId") String teamId) {
        return ResponseEntity.ok(environmentService.getTotalEnvironments());
    }
}
