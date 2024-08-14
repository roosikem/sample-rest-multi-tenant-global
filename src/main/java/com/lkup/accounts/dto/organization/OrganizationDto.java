package com.lkup.accounts.dto.organization;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lkup.accounts.document.Country;
import com.lkup.accounts.document.Organization;
import com.lkup.accounts.dto.environment.EnvironmentDto;
import com.lkup.accounts.dto.team.TeamDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class OrganizationDto {
    private String id;
    private String name;

    private Country country;
    private List<TeamDto> teams;

    @JsonManagedReference
    private List<EnvironmentDto> environments;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
}
