package com.lkup.accounts.controller;

import com.lkup.accounts.document.Role;
import com.lkup.accounts.dto.role.CreateRoleDto;
import com.lkup.accounts.dto.role.RoleDto;
import com.lkup.accounts.dto.role.UpdateRoleDto;
import com.lkup.accounts.mapper.RoleMapper;
import com.lkup.accounts.service.RoleService;
import com.lkup.accounts.utilities.PermissionConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    public RoleController(RoleService roleService, RoleMapper roleMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.CREATE_ROLE + "')")
    public ResponseEntity<RoleDto> createRole(@RequestBody CreateRoleDto createRoleDto) {
        Role role = roleMapper.convertCreateDtoToRole(createRoleDto);
        Role createdRole = roleService.createRole(role);
        if (createdRole != null) {
            RoleDto roleDto = roleMapper.convertRoleToDto(createdRole);
            return ResponseEntity.created(URI.create("/api/v1/roles/" + roleDto.getId())).body(roleDto);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_ROLE + "' ,'" + PermissionConstants.CREATE_ROLE + "')")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable String id) {
        Optional<Role> role = roleService.findRoleById(id);
        return role.map(roleMapper::convertRoleToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_ROLE + "' ,'" + PermissionConstants.CREATE_ROLE + "')")
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<Role> roles = roleService.findAllRoles();
        return ResponseEntity.ok(roleMapper.convertRolesToDtos(roles));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.UPDATE_ROLE + "' ,'" + PermissionConstants.CREATE_ROLE + "')")
    public ResponseEntity<RoleDto> updateRole(@PathVariable String id, @RequestBody UpdateRoleDto updateRoleDto) {
        updateRoleDto.setId(id);
        Role role = roleMapper.updateRoleDtoToRole(updateRoleDto);
        Optional<Role> updatedRole = roleService.updateRole(role);
        return updatedRole.map(roleMapper::convertRoleToDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.DELETE_ROLE + "')")
    public ResponseEntity<Void> deleteRole(@PathVariable String id) {
        boolean isDeleted = roleService.deleteRole(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_ROLE + "' ,'" + PermissionConstants.CREATE_ROLE + "')")
    public ResponseEntity<Long> getTotalRoles() {
        return ResponseEntity.ok(roleService.getTotalRoles());
    }
}
