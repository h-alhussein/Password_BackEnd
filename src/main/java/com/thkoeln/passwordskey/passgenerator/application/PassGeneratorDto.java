package com.thkoeln.passwordskey.passgenerator.application;

import com.thkoeln.passwordskey.passgenerator.domain.PassGenerator;
import com.thkoeln.passwordskey.user.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
public class PassGeneratorDto {
    private int length = 20;
    private boolean upperCase = true;
    private boolean digits = true;
    private boolean space = false;
    private boolean minus = true;
    private boolean underLine = true;
    private boolean special = true;
    private boolean brackets = false;

    public PassGeneratorDto(PassGenerator passGenerator) {
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
