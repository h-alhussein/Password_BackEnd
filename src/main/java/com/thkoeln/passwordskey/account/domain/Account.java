package com.thkoeln.passwordskey.account.domain;

import com.thkoeln.passwordskey.group.domain.Group;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.crypto.SealedObject;
import java.util.Date;

@Data
@Builder
@Document
public class Account {
    @Id
    private String id;
    private String name;
    private String username;
    private String password;
    private String website;
    private String notes;

    private Date createdTime;
    private Date PasswordExpireDate;
    @DBRef
    private Group group;
    private String imageId;
    private boolean fav;
}

