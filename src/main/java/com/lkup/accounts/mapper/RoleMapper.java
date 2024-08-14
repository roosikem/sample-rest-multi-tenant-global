package com.lkup.accounts.mapper;

import com.lkup.accounts.dto.role.CreateRoleDto;
import com.lkup.accounts.dto.role.RoleDto;
import com.lkup.accounts.dto.role.UpdateRoleDto;
import com.lkup.accounts.document.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    private final ModelMapper modelMapper;

    public RoleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public RoleDto convertRoleToDto(Role role) {
        return modelMapper.map(role, RoleDto.class);
    }

    public Role convertCreateDtoToRole(CreateRoleDto createDto) {
        return modelMapper.map(createDto, Role.class);
    }

    public List<RoleDto> convertRolesToDtos(List<Role> roles) {
        return roles.stream().map(this::convertRoleToDto).collect(Collectors.toList());
    }

    public Role updateRoleDtoToRole(UpdateRoleDto updateRoleDto) {
        return modelMapper.map(updateRoleDto, Role.class);
    }
}
