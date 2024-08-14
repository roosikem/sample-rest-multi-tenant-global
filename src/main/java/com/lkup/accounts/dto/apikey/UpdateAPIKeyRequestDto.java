package com.lkup.accounts.dto.apikey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAPIKeyRequestDto {
    private String id;
    private String name;
    private String clientType;
    private String description;
}
