package com.thkoeln.passwordskey.passgenerator.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/passgenerator")
@RequiredArgsConstructor
public class PassGeneratorController {

    private final PassGeneratorService passGeneratorService;

    @GetMapping("/generate")
    public ResponseEntity<?> generate() {
        return passGeneratorService.generateNewPassword();
    }

    @GetMapping
    public ResponseEntity<?> get() {
        return passGeneratorService.getPassGenerator();
    }
  @PostMapping
    public ResponseEntity<?> update(@RequestBody PassGeneratorDto passGeneratorDto) {

        return passGeneratorService.update(passGeneratorDto);
    }



}
