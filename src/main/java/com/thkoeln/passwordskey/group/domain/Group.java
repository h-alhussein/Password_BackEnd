package com.thkoeln.passwordskey.group.domain;

import com.thkoeln.passwordskey.collection.domain.Collection;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@Document
public class Group {
    @Id
    private String id;
    private String name;
    private Date createdTime = new Date();
    @DBRef
    private Collection collection;
    private String imageId;


}
