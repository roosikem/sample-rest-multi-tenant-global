package com.lkup.accounts.controller;


import com.lkup.accounts.document.Environment;
import com.lkup.accounts.dto.environment.EnvironmentDto;
import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.mapper.EnvironmentMapper;
import com.lkup.accounts.service.EnvironmentService;
import com.lkup.accounts.utilities.PermissionConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/markets")
public class MarketsController {

    private final EnvironmentService environmentService;
    private final EnvironmentMapper environmentMapper;

    public MarketsController(EnvironmentService environmentService,
                             EnvironmentMapper environmentMapper) {
        this.environmentService = environmentService;
        this.environmentMapper = environmentMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_CONFIGURATION + "','" + PermissionConstants.CREATE_CONFIGURATION + "')")
    public ResponseEntity<List<OrganizationDto>> getAllOrganizationWithEnv() {
        List<Environment> environments = environmentService.findAllEnvironments();
        List<EnvironmentDto> environmentPojoList = environmentMapper.convertEnvironmentsToDtos(environments);
        Map<OrganizationDto, List<EnvironmentDto>> orgEnvMap = environmentPojoList.stream().filter(env -> Objects.nonNull(env.getOrganizationDto())).collect(Collectors.groupingBy(EnvironmentDto::getOrganizationDto));
        orgEnvMap.forEach(OrganizationDto::setEnvironments);
        List<OrganizationDto> list = orgEnvMap.keySet().stream().toList();
        return ResponseEntity.ok(list);
    }
}
