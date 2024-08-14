package com.lkup.accounts.mapper;

import com.lkup.accounts.document.Organization;
import com.lkup.accounts.document.Team;
import com.lkup.accounts.document.User;
import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.dto.team.CreateTeamDto;
import com.lkup.accounts.dto.team.TeamDto;
import com.lkup.accounts.dto.team.UpdateTeamDto;
import com.lkup.accounts.dto.user.UserTeamsDto;
import com.lkup.accounts.repository.global.OrganizationRepository;
import com.lkup.accounts.repository.global.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeamMapper {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final UserMapper userMapper;

    public TeamMapper(UserRepository userRepository, OrganizationRepository organizationRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.userMapper = userMapper;
    }

    public Team convertCreateTeamDtoToTeam(CreateTeamDto createTeamDto) {
        if (createTeamDto == null) {
            return null;
        }

        Team team = new Team();
        if (createTeamDto.getName() != null) {
            team.setName(createTeamDto.getName());
        }
        if (createTeamDto.getBusinessDetails() != null) {
            team.setBusinessDetails(createTeamDto.getBusinessDetails());
        }
        if (createTeamDto.getBusinessService() != null) {
            team.setBusinessService(createTeamDto.getBusinessService());
        }

        if (createTeamDto.getOrganization() != null) {
            Organization organization = organizationRepository.findById(createTeamDto.getOrganization()).orElse(null);
            team.setOrganization(organization);
        }

        return team;
    }

    public Team convertDtoToTeam(TeamDto teamDto) {
        if (teamDto == null) {
            return null;
        }

        Team team = new Team();
        if (teamDto.getId() != null) {
            team.setId(teamDto.getId());
        }
        if (teamDto.getName() != null) {
            team.setName(teamDto.getName());
        }
        if (teamDto.getBusinessDetails() != null) {
            team.setBusinessDetails(teamDto.getBusinessDetails());
        }
        if (teamDto.getBusinessService() != null) {
            team.setBusinessService(teamDto.getBusinessService());
        }

        if (teamDto.getUsers() != null) {
            List<String> userIds = teamDto.getUsers().stream()
                    .map(UserTeamsDto::getId)
                    .collect(Collectors.toList());

            List<User> users = userRepository.findAllById(userIds);
            team.setUsers(users);
        }

        if (teamDto.getOrganization() != null && teamDto.getOrganization().getName() != null) {
            Organization organization = organizationRepository.findByName(teamDto.getOrganization().getName()).orElse(null);
            team.setOrganization(organization);
        }

        return team;
    }

    public Team convertUpdateTeamDtoToTeam(UpdateTeamDto updateTeamDto) {
        if (updateTeamDto == null) {
            return null;
        }

        Team team = new Team();
        if (updateTeamDto.getId() != null) {
            team.setId(updateTeamDto.getId());
        }
        if (updateTeamDto.getName() != null) {
            team.setName(updateTeamDto.getName());
        }
        if (updateTeamDto.getBusinessDetails() != null) {
            team.setBusinessDetails(updateTeamDto.getBusinessDetails());
        }
        if (updateTeamDto.getBusinessService() != null) {
            team.setBusinessService(updateTeamDto.getBusinessService());
        }

        if (updateTeamDto.getUsers() != null) {
            List<User> users = userRepository.findAllById(updateTeamDto.getUsers());
            team.setUsers(users);
        }

        if (updateTeamDto.getOrganization() != null) {
            Organization organization = organizationRepository.findById(updateTeamDto.getOrganization()).orElse(null);
            team.setOrganization(organization);
        }

        return team;
    }

    public TeamDto convertTeamToDto(Team team) {
        if (team == null) {
            return null;
        }

        TeamDto teamDto = new TeamDto();
        if (team.getId() != null) {
            teamDto.setId(team.getId());
        }
        if (team.getName() != null) {
            teamDto.setName(team.getName());
        }
        if (team.getBusinessDetails() != null) {
            teamDto.setBusinessDetails(team.getBusinessDetails());
        }
        if (team.getBusinessService() != null) {
            teamDto.setBusinessService(team.getBusinessService());
        }
        if (team.getCreatedAt() != null) {
            teamDto.setCreatedAt(team.getCreatedAt());
        }
        if (team.getUpdatedAt() != null) {
            teamDto.setUpdatedAt(team.getUpdatedAt());
        }

        if (team.getUsers() != null) {
            List<UserTeamsDto> userDtos = team.getUsers().stream()
                    .map(userMapper::convertUserToDto)
                    .collect(Collectors.toList());
            teamDto.setUsers(userDtos);
        }

        if (team.getOrganization() != null) {
            OrganizationDto organizationDto = new OrganizationDto();
            if (team.getOrganization().getId() != null) {
                organizationDto.setId(team.getOrganization().getId());
            }
            if (team.getOrganization().getName() != null) {
                organizationDto.setName(team.getOrganization().getName());
            }
            if (team.getOrganization().getCountry() != null) {
                organizationDto.setCountry(team.getOrganization().getCountry());
            }

            if (team.getOrganization().getTeams() != null) {
                List<TeamDto> teamDtos = new ArrayList<>();
                for (Team orgTeam : team.getOrganization().getTeams()) {
                    TeamDto teamDto1 = new TeamDto();
                    if (orgTeam.getId() != null) {
                        teamDto1.setId(orgTeam.getId());
                    }
                    if (orgTeam.getName() != null) {
                        teamDto1.setName(orgTeam.getName());
                    }
                    if (orgTeam.getBusinessDetails() != null) {
                        teamDto1.setBusinessDetails(orgTeam.getBusinessDetails());
                    }
                    if (orgTeam.getBusinessService() != null) {
                        teamDto1.setBusinessService(orgTeam.getBusinessService());
                    }
                    if (orgTeam.getCreatedAt() != null) {
                        teamDto1.setCreatedAt(orgTeam.getCreatedAt());
                    }
                    if (orgTeam.getUpdatedAt() != null) {
                        teamDto1.setUpdatedAt(orgTeam.getUpdatedAt());
                    }
                    teamDtos.add(teamDto1);
                }

                //organizationDto.setTeams(teamDtos);
            }

            if (team.getOrganization().getCreatedAt() != null) {
                organizationDto.setCreatedAt(team.getOrganization().getCreatedAt());
            }
            if (team.getOrganization().getUpdatedAt() != null) {
                organizationDto.setUpdatedAt(team.getOrganization().getUpdatedAt());
            }

            teamDto.setOrganization(organizationDto);
        }

        return teamDto;
    }
}
