package org.springframework.petmanagement.service.impl;

import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.rest.dto.JwtResponseDto;
import org.springframework.petmanagement.rest.dto.SigninRequestDto;
import org.springframework.petmanagement.rest.dto.SignupRequestDto;
import org.springframework.petmanagement.rest.dto.TokenRefreshResponseDto;
import org.springframework.petmanagement.service.AuthService;
import org.springframework.petmanagement.service.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public JwtResponseDto authenticateUser(SigninRequestDto loginRequest) {
        // Simple implementation - in real app, use proper authentication
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new org.springframework.security.authentication.BadCredentialsException("Invalid password");
        }
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, null);
        String accessToken = tokenService.generateToken(auth);
        String refreshToken = tokenService.generateRefreshToken(auth);
        int expiresIn = tokenService.getTokenExpirationSeconds();
        JwtResponseDto response = new JwtResponseDto(accessToken, "Bearer", refreshToken, user.getId());
        response.setExpiresIn(expiresIn);
        return response;
    }

    @Override
    public User registerUser(SignupRequestDto signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getEmail().split("@")[0]); // Generate username from email
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public void logoutUser(String username) {
        // Simple implementation
    }

    @Override
    public TokenRefreshResponseDto refreshToken(String refreshToken) {
        // Simple implementation - in real app, validate refresh token
        Authentication auth = new UsernamePasswordAuthenticationToken("dummy", null, null);
        String newAccessToken = tokenService.generateToken(auth);
        return new TokenRefreshResponseDto(newAccessToken, "new-refresh-token", "Bearer");
    }
}
