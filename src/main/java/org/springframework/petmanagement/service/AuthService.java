package org.springframework.petmanagement.service;

import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.rest.dto.JwtResponseDto;
import org.springframework.petmanagement.rest.dto.SigninRequestDto;
import org.springframework.petmanagement.rest.dto.SignupRequestDto;
import org.springframework.petmanagement.rest.dto.TokenRefreshResponseDto;

public interface AuthService {

    JwtResponseDto authenticateUser(SigninRequestDto loginRequest);

    User registerUser(SignupRequestDto signUpRequest);

    void logoutUser(String email);

    TokenRefreshResponseDto refreshToken(String refreshToken);
}
