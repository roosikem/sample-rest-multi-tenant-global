package com.lkup.accounts.dto.user;

import com.lkup.accounts.document.Team;
import com.lkup.accounts.dto.role.RoleDto;
import com.lkup.accounts.dto.team.TeamDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private String id;
    private String username;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    private RoleDto role;

    private List<TeamDto> teams;
}
