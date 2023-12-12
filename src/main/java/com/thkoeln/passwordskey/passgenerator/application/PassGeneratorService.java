package com.thkoeln.passwordskey.passgenerator.application;


import com.thkoeln.passwordskey.passgenerator.domain.PassGenerator;
import com.thkoeln.passwordskey.passgenerator.domain.PassGeneratorRepository;
import com.thkoeln.passwordskey.user.application.UserService;
import com.thkoeln.passwordskey.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PassGeneratorService {
    public static final String SPACIAL = "~!@#$%^&*+=;,?:/<>|";
    public static final String SPACE = " ";
    public static final String UNDER_LINE = "_";
    public static final String MINUS = "-";
    public static final String BRACKETS = "()[]{}";
    public static final String ERROR_CODE = "ERRONEOUS_CHARS";
    private final PassGeneratorRepository passGeneratorRepository;
    private final UserService userService;


    public ResponseEntity<?> getPassGenerator() {
        User user = userService.getCurrentUser();
        if(user!=null){
            PassGenerator passGenerator = passGeneratorRepository.findByUser(user);

            return new ResponseEntity<>(new PassGeneratorDto(passGenerator),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    public ResponseEntity<?> update(PassGeneratorDto passGeneratorDto) {
        User user = userService.getCurrentUser();
        if(user!=null){
            PassGenerator passGenerator = passGeneratorRepository.findByUser(user);
            passGenerator.update(passGeneratorDto);
            passGeneratorRepository.save(passGenerator);
            return new ResponseEntity<>(generatePassword(passGenerator),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }



    public ResponseEntity<?> generateNewPassword() {
        User user = userService.getCurrentUser();
        PassGenerator passGenerator = passGeneratorRepository.findByUser(user);
        String newPass = generatePassword(passGenerator);
        return new ResponseEntity<>(newPass, HttpStatus.OK);

    }

    public String generatePassword(PassGenerator passGenerator) {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterRule lowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase);
        lowerCaseRule.setNumberOfCharacters(1);
        CharacterRule upperCaseRule = new CharacterRule(EnglishCharacterData.LowerCase);
        upperCaseRule.setNumberOfCharacters(1);
        CharacterRule digitRule = new CharacterRule(EnglishCharacterData.LowerCase);
        digitRule.setNumberOfCharacters(1);
        CharacterRule specialRule = new CharacterRule(EnglishCharacterData.LowerCase);
        CharacterRule spaceRule = new CharacterRule(EnglishCharacterData.LowerCase);
        CharacterRule underLineRule = new CharacterRule(EnglishCharacterData.LowerCase);
        CharacterRule minusRule = new CharacterRule(EnglishCharacterData.LowerCase);
        CharacterRule bracketsRule = new CharacterRule(EnglishCharacterData.LowerCase);

        if (passGenerator.isUpperCase())
            upperCaseRule = new CharacterRule(EnglishCharacterData.UpperCase);
        if (passGenerator.isDigits())
            digitRule = new CharacterRule(EnglishCharacterData.Digit);
        if (passGenerator.isSpace())
            spaceRule = getSpecialRole(SPACE);
        if (passGenerator.isSpecial())
            specialRule = getSpecialRole(SPACIAL);
        if (passGenerator.isUnderLine())
            underLineRule = getSpecialRole(UNDER_LINE);
        if (passGenerator.isMinus())
            minusRule = getSpecialRole(MINUS);
        if (passGenerator.isBrackets())
            bracketsRule = getSpecialRole(BRACKETS);

        String password = gen.generatePassword(passGenerator.getLength(),
                lowerCaseRule,
                upperCaseRule,
                digitRule,
                specialRule,
                spaceRule,
                underLineRule,
                minusRule,
                bracketsRule);
        return password;
    }

    private CharacterRule getSpecialRole(String characters) {
        CharacterData chars = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return characters;
            }
        };
        CharacterRule charRule = new CharacterRule(chars);
        charRule.setNumberOfCharacters(1);
        return charRule;
    }



}
