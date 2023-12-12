package com.thkoeln.passwordskey.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto {


    @NotEmpty(message = "Password is required.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&)(])[A-Za-z\\d@#$!%*?&)(]{8,}$",
            message = "Password must be minimum eight characters, at least one uppercase letter, " +
                    "one lowercase letter, one number and one special character")
    private String password;
    @NotEmpty(message = "Confirm Password is required.")
    private String confirmPass;
    @NotEmpty(message = "The question answer is required.")
    @Size(min = 2, max = 20, message = "The length of question answer must be between 2 and 20 characters.")
    private String questionAnswer;
    private String resetCode;

}
