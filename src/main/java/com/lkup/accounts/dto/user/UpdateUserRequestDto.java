package com.lkup.accounts.dto.user;

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
