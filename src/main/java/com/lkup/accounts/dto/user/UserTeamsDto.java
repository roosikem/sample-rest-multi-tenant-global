package com.lkup.accounts.dto.user;

import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.dto.role.RoleDto;
import com.lkup.accounts.dto.team.TeamNoUsersDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTeamsDto {
    private String id;
    private String username;

    private OrganizationDto organization;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    private RoleDto role;
    private List<String> teams;
}
