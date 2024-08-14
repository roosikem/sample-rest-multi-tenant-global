package com.lkup.accounts.controller;

import com.lkup.accounts.document.Team;
import com.lkup.accounts.document.User;
import com.lkup.accounts.dto.team.*;
import com.lkup.accounts.exceptions.user.UserNotFoundException;
import com.lkup.accounts.mapper.TeamMapper;
import com.lkup.accounts.service.TeamService;
import com.lkup.accounts.service.UserService;
import com.lkup.accounts.utilities.PermissionConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final UserService userService;

    public TeamController(TeamService teamService, TeamMapper teamMapper, UserService userService) {
        this.teamService = teamService;
        this.teamMapper = teamMapper;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "','" + PermissionConstants.CREATE_TEAM + "')")
    public ResponseEntity<TeamDto> createTeam(@RequestBody CreateTeamDto createTeamDto){
        Team team = teamMapper.convertCreateTeamDtoToTeam(createTeamDto);
        Team createdTeam = teamService.createTeam(team, createTeamDto.getOrganization());
        TeamDto teamDto = teamMapper.convertTeamToDto(createdTeam);
        return ResponseEntity.status(HttpStatus.CREATED).body(teamDto);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_TEAM + "' ,'" + PermissionConstants.CREATE_TEAM + "')")
    public ResponseEntity<List<TeamDto>> getAllTeams(){
        List<Team> allTeams = teamService.findAllTeams();
        List<TeamDto> allTeamDtos = allTeams.stream()
                .map(teamMapper::convertTeamToDto)
                .toList();
        return ResponseEntity.ok().body(allTeamDtos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.UPDATE_TEAM + "' ,'" + PermissionConstants.CREATE_TEAM + "')")
    public ResponseEntity<TeamDto> updateTeam(@PathVariable String id, @RequestBody UpdateTeamDto updateTeamDto) {
        updateTeamDto.setId(id);
        Team team = teamMapper.convertUpdateTeamDtoToTeam(updateTeamDto);
        List<String> userIds = updateTeamDto.getUsers();
        Optional<Team> updatedTeam = teamService.updateTeam(team, userIds, id, updateTeamDto.getOrganization());
        if (updatedTeam.isPresent()) {
            TeamDto teamDto = teamMapper.convertTeamToDto(updatedTeam.get());
            return ResponseEntity.ok().body(teamDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_TEAM + "' ,'" + PermissionConstants.CREATE_TEAM + "')")
    public ResponseEntity<TeamDto> getTeamById(@PathVariable String id) {
        Optional<Team> team = teamService.findTeamById(id);
        if (team.isPresent()) {
            TeamDto teamDto = teamMapper.convertTeamToDto(team.get());
            return ResponseEntity.ok().body(teamDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.DELETE_TEAM + "' ,'" + PermissionConstants.CREATE_TEAM + "')")
    public ResponseEntity<Void> deleteTeam(@PathVariable String id) {
        try {
            teamService.deleteTeam(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyAuthority('" + PermissionConstants.ADMINISTRATOR + "', '" + PermissionConstants.VIEW_TEAM + "' ,'" + PermissionConstants.CREATE_TEAM + "')")
    public ResponseEntity<Long> getTotalTeams() {
        return ResponseEntity.ok(teamService.getTotalTeams());
    }

}
