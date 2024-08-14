package com.lkup.accounts.dto.deployment;

import com.lkup.accounts.dto.configuration.ConfigurationDto;
import com.lkup.accounts.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter
@Setter
public class DeploymentDto {

    private String id;

    private String deploymentName;

    private UserDto assignedUser;

    private Boolean status;

    private String publishConfigUrl;

    private ConfigurationDto configuration;

    private Boolean published;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
