package com.lkup.accounts.document;

import lombok.AllArgsConstructor;
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
@Document("deployments")
public class Deployment {

    @Id
    private String id;

    private String deploymentName;

    @DBRef
    private User assigneduser;

    private String organizationId;

    private String teamId;

    @DBRef
    private Configuration configuration;

    private Boolean status = false;

    private String publishConfigUrl;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @Version
    private Integer version;


}
