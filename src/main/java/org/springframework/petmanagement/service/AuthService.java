package org.springframework.petmanagement.service;

import org.springframework.petmanagement.rest.dto.JwtResponseDto;
import org.springframework.petmanagement.rest.dto.LoginRequestDto;
import org.springframework.petmanagement.rest.dto.SignupRequestDto;
import org.springframework.petmanagement.rest.dto.TokenRefreshResponseDto;

public interface AuthService {

    JwtResponseDto authenticateUser(LoginRequestDto loginRequest);

    void registerUser(SignupRequestDto signUpRequest);

    void logoutUser(String username);

    TokenRefreshResponseDto refreshToken(String refreshToken);
}
