package com.thkoeln.passwordskey.securityquestion.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    private String id;
    private String question;

    public Question(String question) {
        this.question = question;
    }
}
