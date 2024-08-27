package com.lkup.accounts.dto.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoleDto {
    private String name;
    private Set<String> permissions;
}
