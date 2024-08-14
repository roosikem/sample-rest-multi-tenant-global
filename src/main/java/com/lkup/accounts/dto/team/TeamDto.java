package com.lkup.accounts.dto.team;

import com.lkup.accounts.document.Organization;
import com.lkup.accounts.dto.organization.OrganizationDto;
import com.lkup.accounts.dto.user.UserDto;
import com.lkup.accounts.dto.user.UserTeamsDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDto {
    private String id;
    private String name;
    private String businessDetails;
    private String businessService;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    private List<UserTeamsDto> users;
    private OrganizationDto organization;
}
