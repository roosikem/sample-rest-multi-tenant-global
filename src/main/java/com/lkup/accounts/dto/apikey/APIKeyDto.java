package com.lkup.accounts.dto.apikey;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter
@Setter
public class APIKeyDto {
    private String id;
    private String name;
    private String appId;
    private String clientType;
    private String clientId;
    private String clientSecret;
    private String description;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
}
