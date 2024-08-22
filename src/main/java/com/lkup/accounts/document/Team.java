package com.lkup.accounts.document;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("teams")
public class Team {
    @Id
    private String id;
    private String name;
    private String businessDetails;
    private String businessService;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    @Version
    private Integer version;
    @DBRef
    private List<User> users;
    @DBRef
    private Organization organization;
}
