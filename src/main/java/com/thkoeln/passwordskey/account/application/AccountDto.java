package com.thkoeln.passwordskey.account.application;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AccountDto {

    @NotEmpty(message = "The Group-Id is required.")
    private String groupId;
    @NotEmpty(message = "The account name is required.")
    @Size(min = 2, max = 50, message = "The length of account name must be between 2 and 50 characters.")
    private String name;
    private String username;
    private String password;
    private String website;
    private String notes;
    private MultipartFile image;
    private String ImageUrl;

}
