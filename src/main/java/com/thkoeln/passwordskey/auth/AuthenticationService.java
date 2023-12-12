package com.thkoeln.passwordskey.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thkoeln.passwordskey.collection.domain.Collection;
import com.thkoeln.passwordskey.collection.domain.CollectionRepository;
import com.thkoeln.passwordskey.config.JwtService;
import com.thkoeln.passwordskey.domainprimitives.EMail;
import com.thkoeln.passwordskey.group.domain.Group;
import com.thkoeln.passwordskey.group.domain.GroupRepository;
import com.thkoeln.passwordskey.helper.EmailService;
import com.thkoeln.passwordskey.image.application.ImageService;
import com.thkoeln.passwordskey.passgenerator.domain.PassGenerator;
import com.thkoeln.passwordskey.passgenerator.domain.PassGeneratorRepository;
import com.thkoeln.passwordskey.securityquestion.application.QuestionService;
import com.thkoeln.passwordskey.securityquestion.domain.Question;
import com.thkoeln.passwordskey.token.Token;
import com.thkoeln.passwordskey.token.TokenRepository;
import com.thkoeln.passwordskey.token.TokenType;
import com.thkoeln.passwordskey.user.domain.Role;
import com.thkoeln.passwordskey.user.domain.User;
import com.thkoeln.passwordskey.user.domain.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PassGeneratorRepository passGeneratorRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final QuestionService questionService;
    private final ImageService imageService;
    private final CollectionRepository collectionRepository;
    private final GroupRepository groupRepository;

    public ResponseEntity<?> register(RegisterRequest request) {
        Map<String, List<String>> body = new HashMap<>();
        List<String> errors = new ArrayList<>();
        if (!request.getPassword().equals(request.getConfirmPass()))
            errors.add("Password not match!");
        // validate question
        Question question = questionService.findById(request.getQuestionId());
        if (question == null)
            errors.add("Please select a question!");
        body.put("errors", errors);
        if (repository.findByEmailAddress(request.getEmail()).isPresent())
            errors.add("This E-Mail already in use!");

        if (errors.size() > 0)
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);

        var user = User.builder()
                .fullName(request.getFullName())
                .email(new EMail(request.getEmail()))
                .password(passwordEncoder.encode(request.getPassword()))
                .question(question)
                .questionAnswer(request.getQuestionAnswer())
                .role(Role.USER)
                .enabled(true)
                .build();

        var savedUser = repository.save(user);
        addCollection(savedUser);
        PassGenerator passGenerator = new PassGenerator();
        passGenerator.setUser(savedUser);
        passGeneratorRepository.save(passGenerator);

        // send verification email
        emailService.sendVerifyEmail(savedUser.getEmail());

        return new ResponseEntity<>("Congratulations, Your account is created. We send a verification E-Mail To you.", HttpStatus.CREATED);
    }

    private void addCollection(User user) {
        Collection collection = collectionRepository.save(Collection.builder().name("Default Collection").user(user).build());
        groupRepository.save(Group.builder().name("Default Group").collection(collection).build());

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmailAddress(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        //  revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.repository.findByEmailAddress(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public ResponseEntity<?> verify_email(String code) {
        Optional<User> user = repository.findByEmailVerifyCode(code);
        if (user.isEmpty())
            return new ResponseEntity<>("Invalid verification code.", HttpStatus.CONFLICT);
        user.get().getEmail().setVerified(true);
        repository.save(user.get());
        return new ResponseEntity<>("Your E-Mail is verified.", HttpStatus.OK);
    }

    public ResponseEntity<?> forget_password(ForgetPassRequest request) {
        Optional<User> user = repository.findByEmailAddress(request.getEmail());
        if (user.isEmpty())
            return new ResponseEntity<>("Invalid Email Address.", HttpStatus.CONFLICT);

        user.get().setResetPasswordCode(UUID.randomUUID().toString());
        repository.save(user.get());
        // send rest mail
        emailService.sendRestPassEmail(request.getEmail(), user.get().getResetPasswordCode());

        return new ResponseEntity<>("We send an E-Mail to reset your password.", HttpStatus.OK);

    }

    public ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto) {
        Optional<User> user = repository.findByResetPasswordCode(resetPasswordDto.getResetCode());
        if (user.isEmpty())
            return new ResponseEntity<>("Invalid Reset Password Code.", HttpStatus.CONFLICT);
        if (!user.get().getQuestionAnswer().equals(resetPasswordDto.getQuestionAnswer()))
            return new ResponseEntity<>("Invalid Question Answer .", HttpStatus.CONFLICT);
        if (!resetPasswordDto.getPassword().equals(resetPasswordDto.getConfirmPass()))
            return new ResponseEntity<>("Password not match!", HttpStatus.CONFLICT);

        user.get().setPassword(passwordEncoder.encode(resetPasswordDto.getPassword()));
        repository.save(user.get());
        return new ResponseEntity<>("Your Password has been reset successfully .", HttpStatus.OK);
    }

    public ResponseEntity<?> getSecureQuestionByRestCode(UUID code) {
        Optional<User> user = repository.findByResetPasswordCode(code.toString());
        if (user.isEmpty())
            return new ResponseEntity<>("Invalid Reset password code.", HttpStatus.CONFLICT);
        return new ResponseEntity<>(user.get().getQuestion().getQuestion(), HttpStatus.OK);
    }
}
