package com.thkoeln.passwordskey.collection.domain;

import com.thkoeln.passwordskey.user.domain.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document
public class Collection {
    @Id
    private String id;
    private String name;
    private Date createdTime;
    @DBRef
    private User user;
    private String imageId;


}
