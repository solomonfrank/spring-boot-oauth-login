package com.example.springOAuth.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springOAuth.event.RegistrationCompleteEvent;
import com.example.springOAuth.model.LoginRequest;
import com.example.springOAuth.model.RegisterRequest;
import com.example.springOAuth.response.AuthenticationResponse;
import com.example.springOAuth.service.AuthenticationService;
import com.example.springOAuth.service.VerificationTokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    private final ApplicationEventPublisher applicationEventPublisher;

    Logger logger = Logger.getLogger(AuthController.class.getName());

    @Operation(summary = "Login auth", description = "Authentication endpoint")
    @ApiResponse(responseCode = "200", description = "Successful operation", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        var authResponse = authenticationService.authenticate(request);

        return ResponseEntity.status(HttpStatus.OK).body(authResponse);

    }

    @Operation(summary = "Register auth", description = "Authentication endpoint")
    @ApiResponse(responseCode = "201", description = "Successful operation", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))
    })

    @ApiResponse(responseCode = "400", description = "Failed")
    @PostMapping("/register")
    public ResponseEntity<?> postMethodName(@Valid @RequestBody RegisterRequest entity, HttpServletRequest request)
            throws MessagingException {

        // Map<String, Object> response = new HashMap<>();
        var response = authenticationService.registerHandler(entity);
        var targetUrl = applicationUrl(request);

        log.info("starting sendEmailVerification ");

        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(entity.getEmail(), targetUrl));

        log.info("End sendEmailVerification ");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        if (token == null) {
            throw new UsernameNotFoundException("Token not found");

        }
        verificationTokenService.verifiyEmailHandler(token);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Email verified successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

    }

}
