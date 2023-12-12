package com.thkoeln.passwordskey.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody @Valid RegisterRequest request
    ) {
        return (service.register(request));
    }

    @PostMapping("/verify-email/{code}")
    public ResponseEntity<?> verify_email(
            @PathVariable String code
    ) {
        return (service.verify_email(code));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> forget_password(
            @RequestBody ForgetPassRequest request
    ) {
        return service.forget_password(request);
    }

    @GetMapping("/secure-question/{code}")
    public ResponseEntity<?> getQuestionByRestPassCode(
            @PathVariable UUID code
    ) {
        return (service.getSecureQuestionByRestCode(code));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody @Valid ResetPasswordDto resetPasswordDto
    ) {
        return (service.resetPassword(resetPasswordDto));
    }


    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }


}
