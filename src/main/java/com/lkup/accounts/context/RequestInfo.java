package com.lkup.accounts.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestInfo {
    private final String requestId;
    private final String teamId;
    private final String tenantId;
}
