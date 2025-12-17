package org.springframework.petmanagement.service.impl;

import org.springframework.petmanagement.mapper.UserMapper;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.rest.dto.JwtResponseDto;
import org.springframework.petmanagement.rest.dto.LoginRequestDto;
import org.springframework.petmanagement.rest.dto.SignupRequestDto;
import org.springframework.petmanagement.rest.dto.TokenRefreshResponseDto;
import org.springframework.petmanagement.service.AuthService;
import org.springframework.petmanagement.service.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, UserMapper userMapper, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public JwtResponseDto authenticateUser(LoginRequestDto loginRequest) {
        // Simple implementation - in real app, use proper authentication
        User user = userRepository.findByUsername(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String token = tokenService.generateToken(null); // Mock authentication
        return new JwtResponseDto(token, "Bearer", "refresh-token", null, user.getEmail(), null);
    }

    @Override
    public void registerUser(SignupRequestDto signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEnabled(true);
        // Set default role if needed
        userRepository.save(user);
    }

    @Override
    public void logoutUser(String username) {
        // Simple implementation
    }

    @Override
    public TokenRefreshResponseDto refreshToken(String refreshToken) {
        // Simple implementation - in real app, validate refresh token
        String newAccessToken = tokenService.generateToken(null); // Mock
        return new TokenRefreshResponseDto(newAccessToken, "new-refresh-token", "Bearer");
    }
}
