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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("environments")
public class Environment {

    @Id
    private String id;
    private String name;

    @DBRef
    private List<APIKey> apiKeys;


    @DBRef
    private List<AppId> appIds;

    private String hostUrl;
    private String authTokenUrl;
    private String defaultConfigTemplate;

    private String organizationId;
    private String teamId;

    private String environmentType;

    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    @Version
    private Integer version;

}
