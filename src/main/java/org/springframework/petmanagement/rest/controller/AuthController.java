package org.springframework.petmanagement.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.UserMapper;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.rest.api.AuthApi;
import org.springframework.petmanagement.rest.dto.JwtResponseDto;
import org.springframework.petmanagement.rest.dto.LoginRequestDto;
import org.springframework.petmanagement.rest.dto.MessageResponseDto;
import org.springframework.petmanagement.rest.dto.SignupRequestDto;
import org.springframework.petmanagement.rest.dto.TokenRefreshRequestDto;
import org.springframework.petmanagement.rest.dto.TokenRefreshResponseDto;
import org.springframework.petmanagement.rest.dto.UserResponseDto;
import org.springframework.petmanagement.service.AuthService;
import org.springframework.petmanagement.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
@Validated
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthController(AuthService authService, TokenService tokenService, UserRepository userRepository, UserMapper userMapper) {
        this.authService = authService;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @PostMapping("/token")
    public String createToken(Authentication authentication) {
        String token = tokenService.generateToken(authentication);
        return token;
    }

    @Override
    public ResponseEntity<JwtResponseDto> authenticateUser(@Valid LoginRequestDto loginRequestDto) {
        JwtResponseDto response = authService.authenticateUser(loginRequestDto);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MessageResponseDto> registerUser(SignupRequestDto signupRequestDto) {
        authService.registerUser(signupRequestDto);
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("User registered successfully!");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TokenRefreshResponseDto> refreshToken(TokenRefreshRequestDto tokenRefreshRequestDto) {
        TokenRefreshResponseDto response = authService.refreshToken(tokenRefreshRequestDto.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MessageResponseDto> logoutUser() {
        // Assume logout is handled by client-side token removal
        MessageResponseDto response = new MessageResponseDto();
        response.setMessage("User logged out successfully!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userName = jwt.getClaimAsString("userName");
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserResponseDto userResponseDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userResponseDto);
    }

    // Simple DTO classes
    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class SignupRequest {
        public String username;
        public String email;
        public String password;
    }

    public static class RefreshRequest {
        public String refreshToken;
    }
}
