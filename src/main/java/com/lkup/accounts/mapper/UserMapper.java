package com.lkup.accounts.mapper;

import com.lkup.accounts.document.Organization;
import com.lkup.accounts.document.Role;
import com.lkup.accounts.document.Team;
import com.lkup.accounts.document.User;
import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.dto.role.RoleDto;
import com.lkup.accounts.dto.team.TeamDto;
import com.lkup.accounts.dto.user.CreateUserDto;
import com.lkup.accounts.dto.user.UpdateUserRequestDto;
import com.lkup.accounts.dto.user.UserTeamsDto;
import com.lkup.accounts.service.OrganizationService;
import com.lkup.accounts.service.RoleService;
import com.lkup.accounts.service.TeamService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserMapper {
    private final OrganizationService organizationService;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final TeamService teamService;

    public UserMapper(ModelMapper modelMapper, OrganizationService organizationService, RoleService roleService,
                      TeamService teamService) {
        this.roleService = roleService;
        this.organizationService = organizationService;
        this.modelMapper = modelMapper;
        this.teamService = teamService;
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
            userDto.setTeams(modelMapper.map(teams, new TypeToken<List<TeamDto>>() {
            }.getType()));
        }

        if (user.getRole() != null) {
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

    public User convertCreateDtoToUser(CreateUserDto createUserDto) {
        if (createUserDto == null) {
            return null;
        }

        User user = new User();
        if (createUserDto.getUsername() != null) {
            user.setUsername(createUserDto.getUsername());
        }

        if (createUserDto.getOrganization() != null) {
            Optional<Organization> organization = organizationService.findOrganizationById(createUserDto.getOrganization());
            organization.ifPresent(user::setOrganization);
        }

        if (createUserDto.getRoleId() != null) {
            Optional<Role> role = roleService.findRoleById(createUserDto.getRoleId());
            role.ifPresent(user::setRole);
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

        if (updateUserRequestDto.getTeams() != null) {
            List<Team> teams = new ArrayList<>();
            for (String teamId : updateUserRequestDto.getTeams()) {
                Optional<Team> teamDb = teamService.findTeamById(teamId);
                if (teamDb.isPresent()) {
                    teams.add(teamDb.get());
                }

            }
            user.setTeams(teams);
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
