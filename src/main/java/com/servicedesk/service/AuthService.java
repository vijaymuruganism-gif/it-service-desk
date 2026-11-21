package com.servicedesk.service;

import com.servicedesk.dto.AuthResponseDto;
import com.servicedesk.dto.LoginRequestDto;
import com.servicedesk.dto.RegisterRequestDto;

/**
 * Service contract for user authentication, registration, and credential verification.
 */
public interface AuthService {

    /**
     * Authenticates credentials and returns a signed JWT.
     */
    AuthResponseDto login(LoginRequestDto request);

    /**
     * Registers a new user with encrypted password and returns a signed JWT.
     */
    AuthResponseDto register(RegisterRequestDto request);
}
