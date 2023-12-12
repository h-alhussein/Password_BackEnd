package com.thkoeln.passwordskey.passgenerator.domain;

import com.thkoeln.passwordskey.passgenerator.application.PassGeneratorDto;
import com.thkoeln.passwordskey.user.domain.User;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document
public class PassGenerator {
    @Id
    private String id;
    private Date createdTime = new Date();
    @DBRef
    private User user;
    private int length = 20;
    private boolean upperCase = true;
    private boolean digits = true;
    private boolean space = false;
    private boolean minus = true;
    private boolean underLine = true;
    private boolean special = true;
    private boolean brackets = false;

    public void update(PassGeneratorDto passGenerator) {
        this.length = passGenerator.getLength();
        this.upperCase = passGenerator.isUpperCase();
        this.digits = passGenerator.isDigits();
        this.minus = passGenerator.isMinus();
        this.space = passGenerator.isSpace();
        this.underLine = passGenerator.isUnderLine();
        this.special = passGenerator.isSpecial();
        this.brackets = passGenerator.isBrackets();
    }
}
