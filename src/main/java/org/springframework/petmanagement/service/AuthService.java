package org.springframework.petmanagement.service;

import org.springframework.petmanagement.rest.dto.JwtResponseDto;
import org.springframework.petmanagement.rest.dto.LoginRequestDto;
import org.springframework.petmanagement.rest.dto.SignupRequestDto;

public interface AuthService {

    JwtResponseDto authenticateUser(LoginRequestDto loginRequest);

    void registerUser(SignupRequestDto signUpRequest);

    void logoutUser(String username);

    JwtResponseDto refreshToken(String refreshToken);
}
