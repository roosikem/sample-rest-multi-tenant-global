package com.lkup.accounts.mapper;

import com.lkup.accounts.document.*;
import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.dto.role.RoleDto;
import com.lkup.accounts.dto.team.TeamDto;
import com.lkup.accounts.dto.team.TeamNoUsersDto;
import com.lkup.accounts.dto.user.CreateUserDto;
import com.lkup.accounts.dto.user.UpdateUserRequestDto;
import com.lkup.accounts.dto.user.UserDto;
import com.lkup.accounts.dto.user.UserTeamsDto;
import com.lkup.accounts.service.OrganizationService;
import com.lkup.accounts.service.RoleService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserMapper {
    private final OrganizationService organizationService;
    private final RoleService roleService;

    public UserMapper(OrganizationService organizationService, RoleService roleService) {
        this.roleService = roleService;
        this.organizationService = organizationService;
    }

    public UserTeamsDto convertUserToDto(User user) {
        if (user == null) {
            return null;
        }

        UserTeamsDto userDto = new UserTeamsDto();
        if (user.getId() != null) {
            userDto.setId(user.getId());
        }
        if (user.getUsername() != null) {
            userDto.setUsername(user.getUsername());
        }
        if (user.getCreatedAt() != null) {
            userDto.setCreatedAt(user.getCreatedAt());
        }
        if (user.getUpdatedAt() != null) {
            userDto.setUpdatedAt(user.getUpdatedAt());
        }

        OrganizationDto organizationDto = new OrganizationDto();
        if (user.getOrganization() != null) {
            if (user.getOrganization().getId() != null) {
                organizationDto.setId(user.getOrganization().getId());
            }
            if (user.getOrganization().getName() != null) {
                organizationDto.setName(user.getOrganization().getName());
            }
            if (user.getOrganization().getCountry() != null) {
                organizationDto.setCountry(user.getOrganization().getCountry());
            }
            if (user.getOrganization().getUpdatedAt() != null) {
                organizationDto.setUpdatedAt(user.getOrganization().getUpdatedAt());
            }
            if (user.getOrganization().getCreatedAt() != null) {
                organizationDto.setCreatedAt(user.getOrganization().getCreatedAt());
            }

            userDto.setOrganization(organizationDto);
        }

        List<Team> teams = user.getTeams();
        if (teams != null && !teams.isEmpty()) {
            List<String> teamNames = new ArrayList<>();
            for (Team team : teams) {
                if (team.getName() != null) {
                    teamNames.add(team.getName());
                }
            }
            userDto.setTeams(teamNames);
        }

        if(user.getRole() != null){
            RoleDto roleDto = new RoleDto();
            if (user.getRole().getId() != null) {
                roleDto.setId(user.getRole().getId());
            }
            if (user.getRole().getName() != null) {
                roleDto.setName(user.getRole().getName());
            }
            if (user.getRole().getPermissions() != null) {
                roleDto.setPermissions(user.getRole().getPermissions());
            }
            if (user.getRole().getUpdatedAt() != null) {
                roleDto.setUpdatedAt(user.getRole().getUpdatedAt());
            }
            if (user.getRole().getCreatedAt() != null) {
                roleDto.setCreatedAt(user.getRole().getCreatedAt());
            }

            userDto.setRole(roleDto);
        }
        return userDto;
    }

    public User convertCreateDtoToUser(CreateUserDto createUserDto){
        if (createUserDto == null) {
            return null;
        }

        User user = new User();
        if (createUserDto.getUsername() != null) {
            user.setUsername(createUserDto.getUsername());
        }

        if (createUserDto.getOrganization() != null) {
            Optional<Organization> organization = organizationService.findOrganizationById(createUserDto.getOrganization());
            if (organization.isPresent()) {
                user.setOrganization(organization.get());
            }
        }

        if (createUserDto.getRoleId() != null) {
            Optional<Role> role = roleService.findRoleById(createUserDto.getRoleId());
            if (role.isEmpty()) {
                role = roleService.findRoleById("bbc412cb-b430-47f1-8010-6ad4a27a0237");
            }
            if (role.isPresent()) {
                user.setRole(role.get());
            }
        }
        return user;
    }

    public UpdateUserRequestDto convertUserToUpdateDto(User user) {
        if (user == null) {
            return null;
        }

        UpdateUserRequestDto updateUserDto = new UpdateUserRequestDto();
        if (user.getId() != null) {
            updateUserDto.setId(user.getId());
        }
        if (user.getUsername() != null) {
            updateUserDto.setUsername(user.getUsername());
        }
        return updateUserDto;
    }

    public User convertUpdateDtoToUser(UpdateUserRequestDto updateUserRequestDto) {
        if (updateUserRequestDto == null) {
            return null;
        }

        User user = new User();
        if (updateUserRequestDto.getId() != null) {
            user.setId(updateUserRequestDto.getId());
        }
        if (updateUserRequestDto.getUsername() != null) {
            user.setUsername(updateUserRequestDto.getUsername());
        }
        if (updateUserRequestDto.getOrganization() != null) {
            Optional<Organization> organization = organizationService.findOrganizationById(updateUserRequestDto.getOrganization());
            if (organization.isPresent()) {
                user.setOrganization(organization.get());
            }
        }

        return user;
    }

    public CreateUserDto convertUserToCreateDto(User user) {
        if (user == null) {
            return null;
        }

        CreateUserDto createUserDto = new CreateUserDto();
        if (user.getUsername() != null) {
            createUserDto.setUsername(user.getUsername());
        }
        return createUserDto;
    }

    public Iterable<UserTeamsDto> convertUsersToDtos(Iterable<User> users) {
        if (users == null) {
            return null;
        }

        List<UserTeamsDto> userDtos = new ArrayList<>();
        for (User user : users) {
            if (user != null) {
                userDtos.add(convertUserToDto(user));
            }
        }
        return userDtos;
    }
}
