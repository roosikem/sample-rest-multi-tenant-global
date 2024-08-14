package com.lkup.accounts.controller;

import com.lkup.accounts.document.Configuration;
import com.lkup.accounts.dto.configuration.ConfigurationDto;
import com.lkup.accounts.dto.configuration.CreateConfigurationDto;
import com.lkup.accounts.dto.configuration.UpdateConfigurationDto;
import com.lkup.accounts.mapper.ConfigurationMapper;
import com.lkup.accounts.service.ConfigurationService;
import com.lkup.accounts.utilities.PermissionConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Tag(name = "Configurations API")
@RestController
@RequestMapping("/api/v1/configurations")
public class ConfigurationController {

    private final ConfigurationService configurationService;
    private final ConfigurationMapper configurationMapper;

    public ConfigurationController(ConfigurationService configurationService, ConfigurationMapper configurationMapper) {
        this.configurationService = configurationService;
        this.configurationMapper = configurationMapper;
    }

    @Operation(summary = "List Configuration", description = "Returns a List of Configuration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_CONFIGURATION + "', '" + PermissionConstants.CREATE_CONFIGURATION + "')")
    public ResponseEntity<List<ConfigurationDto>> getAllConfigurations() {
        List<Configuration> configurations = configurationService.findAllConfigurations();
        return ResponseEntity.ok(configurationMapper.convertConfigurationsToDtos(configurations));
    }


    @Operation(summary = "Get a configuration by id", description = "Returns a configuration as per the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Not found - The configuration was not found")
    })
    @PostMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', " + "'" + PermissionConstants.CREATE_CONFIGURATION + "')")
    public ResponseEntity<ConfigurationDto> createConfiguration(@RequestBody @Validated CreateConfigurationDto createConfigurationDto) {
        Configuration configuration = configurationMapper.convertCreateDtoToConfiguration(createConfigurationDto);
        Configuration createConfiguration = configurationService.createConfiguration(configuration);
        ConfigurationDto responseConfigurationDto = configurationMapper.convertConfigurationToDto(createConfiguration);
        return ResponseEntity.created(URI.create("/api/v1/configuration/" + responseConfigurationDto.getId())).body(responseConfigurationDto);
    }

    @Operation(summary = "Create Configuration API", description = "Returns a created Configuration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_CONFIGURATION + "','" + PermissionConstants.CREATE_CONFIGURATION + "')")
    public ResponseEntity<ConfigurationDto> getConfigurationById(@PathVariable String id) {
        Optional<Configuration> configuration = configurationService.findConfigurationById(id);
        return configuration.map(configurationMapper::convertConfigurationToDto).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update Configuration API", description = "Returns a updated Configuration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.UPDATE_CONFIGURATION + "')")
    public ResponseEntity<ConfigurationDto> updateConfiguration(@PathVariable String id, @RequestBody @Validated UpdateConfigurationDto updateConfigurationDto) {
        updateConfigurationDto.setId(id);
        Optional<Configuration> updateConfiguration = configurationService.updateConfiguration(configurationMapper.convertUpdateDtoToConfiguration(updateConfigurationDto));
        return updateConfiguration.map(configurationMapper::convertConfigurationToDto).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


}
