package com.servicedesk.controller;

import com.servicedesk.dto.AuthResponseDto;
import com.servicedesk.dto.LoginRequestDto;
import com.servicedesk.dto.RegisterRequestDto;
import com.servicedesk.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public REST Controller exposing authentication endpoints (login and registration).
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and JWT login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates a user and returns a signed JWT token.
     */
    @PostMapping("/login")
    @Operation(summary = "Login to obtain a JWT Bearer token", description = "Authenticate using username and password to receive a JWT access token.")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Registers a new user account with BCrypt password hashing and returns a signed JWT.
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user account", description = "Create an EMPLOYEE, SUPPORT_ENGINEER, or ADMIN account with encrypted password.")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        AuthResponseDto response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
