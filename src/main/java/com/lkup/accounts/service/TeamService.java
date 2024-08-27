package com.lkup.accounts.service;

import com.lkup.accounts.document.Organization;
import com.lkup.accounts.document.Team;
import com.lkup.accounts.exceptions.BadRequestException;
import com.lkup.accounts.exceptions.organization.OrganizationNotFoundException;
import com.lkup.accounts.exceptions.team.TeamNotFoundException;
import com.lkup.accounts.repository.global.OrganizationRepository;
import com.lkup.accounts.repository.global.TeamRepository;
import com.lkup.accounts.repository.global.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private static final Logger logger = LoggerFactory.getLogger(TeamService.class);
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final DefaultUUIDGeneratorService defaultUUIDGenerator;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository, OrganizationRepository organizationRepository, DefaultUUIDGeneratorService defaultUUIDGenerator) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.defaultUUIDGenerator = defaultUUIDGenerator;
    }

    public Team createTeam(Team team, String organizationId) {
        team.setId(defaultUUIDGenerator.generateId());
        //List<User> users = userRepository.findAllById(userIds);
        //team.setUsers(users);

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + organizationId + " not found"));

        Optional<Team> teamDb = teamRepository.findByNameAndOrganizationId(team.getName(), team.getOrganization().getId());
        if (teamDb.isPresent()) {
            throw new BadRequestException("Team with name already exist - " + team.getName() + "");
        }
        team.setOrganization(organization);
        /*users.forEach(user -> {
            List<Team> userTeams = Optional.ofNullable(user.getTeams()).orElse(new ArrayList<>());
            userTeams.add(team);
            user.setTeams(userTeams);
        });*/

        Team savedTeam = teamRepository.save(team);
        //userRepository.saveAll(users);

        return savedTeam;
    }

    public Optional<Team> findTeamById(String id) {
        return teamRepository.findById(id);
    }

    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    public Optional<Team> updateTeam(Team team, List<String> userIds, String id, String organizationId) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException("Team with id " + team.getId() + " not found"));
        Optional<Team> teamDb = teamRepository.findByNameAndOrganizationId(team.getName(), team.getOrganization().getId());
        if (teamDb.isPresent() && !teamDb.get().getId().equals(id)) {
            throw new BadRequestException("Team with name already exist - " + team.getName() + "");
        }
        team.setId(null);  // Set the id of the team object to null
        BeanUtils.copyProperties(team, existingTeam, "_id", "users");

        if (organizationId != null && !organizationId.isEmpty() && !organizationId.isBlank()) {
            Organization organization = organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new OrganizationNotFoundException("Organization with id " + organizationId + " not found"));
            existingTeam.setOrganization(organization);
        }

        return Optional.of(teamRepository.save(existingTeam));
    }

    public long getTotalTeams() {
        return teamRepository.count();
    }

    public void deleteTeam(String id) {
        teamRepository.deleteById(id);
        logger.info("Team with id {} deleted successfully", id);
    }

    public void deleteAll() {
        teamRepository.deleteAll();
    }

    public Optional<Team> findByIdAndOrganizationId(String teamId, String orgId) {
        return teamRepository.findByIdAndOrganizationId(teamId, orgId);
    }
}
