package com.lkup.accounts.controller;

import com.lkup.accounts.document.Organization;
import com.lkup.accounts.dto.organization.CreateOrganizationDto;
import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.dto.organization.UpdateOrganizationDto;
import com.lkup.accounts.mapper.OrganizationMapper;
import com.lkup.accounts.service.OrganizationService;
import com.lkup.accounts.utilities.PermissionConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationMapper organizationMapper;

    public OrganizationController(OrganizationService organizationService, OrganizationMapper organizationMapper) {
        this.organizationService = organizationService;
        this.organizationMapper = organizationMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.CREATE_ORGANIZATION + "')")
    public ResponseEntity<OrganizationDto> createOrganization(@RequestBody CreateOrganizationDto organizationDto) {
        Organization organization = organizationMapper.convertCreateDtoToOrganization(organizationDto);
        Organization createdOrganization = organizationService.createOrganization(organization);
        OrganizationDto responseOrganizationDto = organizationMapper.convertOrganizationToDto(createdOrganization);
        return ResponseEntity.created(URI.create("/api/v1/organizations/" + responseOrganizationDto.getId())).body(responseOrganizationDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_ORGANIZATION + "' ,'" + PermissionConstants.CREATE_ORGANIZATION + "')")
    public ResponseEntity<OrganizationDto> getOrganizationById(@PathVariable String id) {
        Optional<Organization> organization = organizationService.findOrganizationById(id);
        return organization.map(organizationMapper::convertOrganizationToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_ORGANIZATION + "' ,'" + PermissionConstants.CREATE_ORGANIZATION + "')")
    public ResponseEntity<Iterable<OrganizationDto>> getAllOrganizations() {
        Iterable<Organization> organizations = organizationService.findAllOrganizations();
        return ResponseEntity.ok(organizationMapper.convertOrganizationsToDtos(organizations));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.UPDATE_ORGANIZATION + "' ,'" + PermissionConstants.CREATE_ORGANIZATION + "')")
    public ResponseEntity<OrganizationDto> updateOrganization(@PathVariable String id, @RequestBody UpdateOrganizationDto organizationDto) {
        organizationDto.setId(id);
        Optional<Organization> updatedOrganization = organizationService.updateOrganization(organizationMapper.convertUpdateDtoToOrganization(organizationDto));
        return updatedOrganization.map(organizationMapper::convertOrganizationToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.DELETE_ORGANIZATION + "')")
    public ResponseEntity<Void> deleteOrganization(@PathVariable String id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_ORGANIZATION + "' ,'" + PermissionConstants.CREATE_ORGANIZATION + "')")
    public ResponseEntity<Long> getTotalOrganizations() {
        return ResponseEntity.ok(organizationService.getTotalOrganizations());
    }
}
