package com.lkup.accounts.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {
    private String username;
    private String roleId;
    private String organization;

}
