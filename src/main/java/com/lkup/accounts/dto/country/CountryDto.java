package com.lkup.accounts.dto.country;

import com.lkup.accounts.dto.team.TeamDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CountryDto {
    private String id;
    private String name;
    private String code;
}
