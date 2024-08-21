package com.lkup.accounts.dto.team;


import com.lkup.accounts.dto.organization.OrganizationDto;
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
    private OrganizationDto organization;
}
