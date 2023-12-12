package com.thkoeln.passwordskey.group.application;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class GroupDto {

    @NotEmpty(message = "The Collection-Id is required.")
    private String collectionId;

    @NotEmpty(message = "The group name is required.")
    @Size(min = 2, max = 50, message = "The length of  group name must be between 2 and 50 characters.")
    private String name;
    private MultipartFile image;
    private String ImageUrl;


}
