package com.lkup.accounts.dto.organization;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrganizationDto {
    private String id;
    private String name;
    private String country;
}
