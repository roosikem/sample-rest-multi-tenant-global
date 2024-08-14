package com.lkup.accounts.dto.team;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateTeamDto {
    private String id;
    private String name;
    private String businessDetails;
    private String businessService;
    private List<String> users;
    private String organization;
}
