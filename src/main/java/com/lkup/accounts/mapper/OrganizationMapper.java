package com.lkup.accounts.mapper;

import com.lkup.accounts.document.Organization;
import com.lkup.accounts.document.Team;
import com.lkup.accounts.dto.organization.CreateOrganizationDto;
import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.dto.organization.UpdateOrganizationDto;
import com.lkup.accounts.dto.team.TeamDto;
import com.lkup.accounts.repository.global.CountryRepository;
import com.lkup.accounts.repository.global.TeamRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrganizationMapper {

    private final TeamMapper teamMapper;
    private final TeamRepository teamRepository;
    private final CountryRepository countryRepository;

    public OrganizationMapper(TeamMapper teamMapper, TeamRepository teamRepository, CountryRepository countryRepository) {
        this.teamMapper = teamMapper;
        this.teamRepository = teamRepository;
        this.countryRepository = countryRepository;
    }

    public OrganizationDto convertOrganizationToDto(Organization organization) {
        if (organization == null) {
            return null;
        }

        OrganizationDto organizationDto = new OrganizationDto();
        if (organization.getId() != null) {
            organizationDto.setId(organization.getId());
        }
        if (organization.getName() != null) {
            organizationDto.setName(organization.getName());
        }
        if (organization.getCountry() != null) {
            organizationDto.setCountry(organization.getCountry());
        }
        if (organization.getCreatedAt() != null) {
            organizationDto.setCreatedAt(organization.getCreatedAt());
        }
        if (organization.getUpdatedAt() != null) {
            organizationDto.setUpdatedAt(organization.getUpdatedAt());
        }

        if (organization.getId() != null) {
            List<Team> teams = teamRepository.findTeamsByOrganizationId(organization.getId());

            List<TeamDto> teamDtos = new ArrayList<>();
            for (Team team : teams) {
                if (team != null) {
                    TeamDto teamDto = new TeamDto();
                    if (team.getId() != null) {
                        teamDto.setId(team.getId());
                    }
                    if (team.getName() != null) {
                        teamDto.setName(team.getName());
                    }
                    teamDtos.add(teamDto);
                }
            }
            organizationDto.setTeams(teamDtos);
        }

        return organizationDto;
    }

    public Organization convertDtoToOrganization(OrganizationDto organizationDto) {
        if (organizationDto == null) {
            return null;
        }

        Organization organization = new Organization();
        if (organizationDto.getId() != null) {
            organization.setId(organizationDto.getId());
        }
        if (organizationDto.getName() != null) {
            organization.setName(organizationDto.getName());
        }

        if (organizationDto.getTeams() != null) {
            List<TeamDto> teamDtos = organizationDto.getTeams();
            List<Team> teams = new ArrayList<>();
            for (TeamDto teamDto : teamDtos) {
                if (teamDto != null) {
                    teams.add(teamMapper.convertDtoToTeam(teamDto));
                }
            }
            organization.setTeams(teams);
        }

        return organization;
    }

    public Organization convertCreateDtoToOrganization(CreateOrganizationDto createOrganizationDto) {
        if (createOrganizationDto == null) {
            return null;
        }

        Organization organization = new Organization();
        if (createOrganizationDto.getId() != null) {
            organization.setId(createOrganizationDto.getId());
        }
        if (createOrganizationDto.getName() != null) {
            organization.setName(createOrganizationDto.getName());
        }
        if (createOrganizationDto.getCountry() != null) {
            organization.setCountry(countryRepository.findById(createOrganizationDto.getCountry()).orElse(null));
        }

        return organization;
    }

    public Organization convertUpdateDtoToOrganization(UpdateOrganizationDto updateOrganizationDto) {
        if (updateOrganizationDto == null) {
            return null;
        }

        Organization organization = new Organization();
        if (updateOrganizationDto.getId() != null) {
            organization.setId(updateOrganizationDto.getId());
        }
        if (updateOrganizationDto.getName() != null) {
            organization.setName(updateOrganizationDto.getName());
        }
        if (updateOrganizationDto.getCountry() != null) {
            organization.setCountry(countryRepository.findById(updateOrganizationDto.getCountry()).orElse(null));
        }

        return organization;
    }

    public Iterable<OrganizationDto> convertOrganizationsToDtos(Iterable<Organization> organizations) {
        if (organizations == null) {
            return null;
        }

        List<OrganizationDto> organizationDtos = new ArrayList<>();
        for (Organization organization : organizations) {
            if (organization != null) {
                organizationDtos.add(convertOrganizationToDto(organization));
            }
        }
        return organizationDtos;
    }

    public Organization convertOrgIdToOrganization(@NotNull String orgId) {
        if (orgId == null) {
            return null;
        }

        Organization organization = new Organization();
        organization.setId(orgId);
        return organization;
    }
}
