package com.lkup.accounts.dto.appId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppIdDto {
    private String id;
    private String name;
    private String appId;
    private String description;
    private String organization;
    private String team;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;


    public AppIdDto(String name, String appId, String description, String organization, String team){
        this.name = name;
        this.appId = appId;
        this.description = description;
        this.organization = organization;
        this.team = team;
    }
}
