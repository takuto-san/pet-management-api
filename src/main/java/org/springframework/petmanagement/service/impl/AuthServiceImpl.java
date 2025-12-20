package org.springframework.petmanagement.service.impl;

import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.RefreshTokenRepository;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.rest.dto.JwtResponseDto;
import org.springframework.petmanagement.rest.dto.SigninRequestDto;
import org.springframework.petmanagement.rest.dto.SignupRequestDto;
import org.springframework.petmanagement.rest.dto.TokenRefreshResponseDto;
import org.springframework.petmanagement.service.AuthService;
import org.springframework.petmanagement.service.TokenService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public JwtResponseDto authenticateUser(SigninRequestDto loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities());
        String accessToken = tokenService.generateToken(auth);
        String refreshToken = tokenService.generateRefreshToken(auth);
        int expiresIn = tokenService.getTokenExpirationSeconds();
        JwtResponseDto response = new JwtResponseDto(accessToken, "Bearer", refreshToken, user.getId());
        response.setExpiresIn(expiresIn);
        return response;
    }

    @Override
    @Transactional
    public User registerUser(SignupRequestDto signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = new User();
        user.setUsername(signUpRequest.getEmail()); // Use full email as username
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void logoutUser(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    @Transactional
    public TokenRefreshResponseDto refreshToken(String refreshToken) {
        Authentication auth = tokenService.validateRefreshToken(refreshToken);
        tokenService.deleteRefreshToken(refreshToken);
        String newAccessToken = tokenService.generateToken(auth);
        String newRefreshToken = tokenService.generateRefreshToken(auth);
        return new TokenRefreshResponseDto(newAccessToken, newRefreshToken, "Bearer");
    }

    @Override
    public User getCurrentUser(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
