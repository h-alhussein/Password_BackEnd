package com.thkoeln.passwordskey.collection.application;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CollectionDto {
    @NotEmpty(message = "The collection name is required.")
    @Size(min = 2, max = 50, message = "The length of collection name must be between 2 and 50 characters.")
    private String name;
    private MultipartFile image;
    private String ImageUrl;


}
