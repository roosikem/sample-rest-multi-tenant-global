package com.lkup.accounts.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("configurations")
public class Configuration {
    @Id
    private String id;
    private String name;
    private String description;

    private String organizationId;

    private String teamId;

    private String widgetColor;

    private String alignment;

    private String sideSpace;

    private String bottomSpace;

    private Integer launcherButtonVisibility;

    private String status;

    private Object widgetConfig;

    private String configUrl;

    @DBRef
    private Environment environment;

    @DBRef
    private AppId appId;

    private String hostUrl;

    private String authTokenUrl;

    private String language;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @Version
    private Integer version;
}
