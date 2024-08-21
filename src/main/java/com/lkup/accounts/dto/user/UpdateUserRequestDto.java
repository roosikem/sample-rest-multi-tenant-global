package com.lkup.accounts.dto.user;

import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.dto.team.TeamDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateUserRequestDto {
    private String id;
    private String username;
    private String organization;
    private List<String> teams;
}
