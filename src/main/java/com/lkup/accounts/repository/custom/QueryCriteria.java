package com.lkup.accounts.repository.custom;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryCriteria {

    @NotNull
    private String tenantId;

    @NotNull
    private String teamId;
}
