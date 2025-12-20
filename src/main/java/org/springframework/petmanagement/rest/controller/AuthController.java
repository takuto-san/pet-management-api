package org.springframework.petmanagement.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.UserMapper;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.rest.api.AuthApi;
import org.springframework.petmanagement.rest.dto.JwtResponseDto;
import org.springframework.petmanagement.rest.dto.SigninRequestDto;
import org.springframework.petmanagement.rest.dto.SignoutResponseDto;
import org.springframework.petmanagement.rest.dto.SignupRequestDto;
import org.springframework.petmanagement.rest.dto.SignupResponseDto;
import org.springframework.petmanagement.rest.dto.TokenRefreshRequestDto;
import org.springframework.petmanagement.rest.dto.TokenRefreshResponseDto;
import org.springframework.petmanagement.rest.dto.UserResponseDto;
import org.springframework.petmanagement.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
@Validated
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AuthController(AuthService authService, UserRepository userRepository, UserMapper userMapper) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<JwtResponseDto> authenticateUser(@Valid SigninRequestDto loginRequestDto) {
        JwtResponseDto response = authService.authenticateUser(loginRequestDto);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SignupResponseDto> registerUser(@Valid SignupRequestDto signupRequestDto) {
        User user = authService.registerUser(signupRequestDto);
        SignupResponseDto response = userMapper.toSignupResponseDto(user);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<TokenRefreshResponseDto> refreshToken(TokenRefreshRequestDto tokenRefreshRequestDto) {
        TokenRefreshResponseDto response = authService.refreshToken(tokenRefreshRequestDto.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SignoutResponseDto> logoutUser() {
        SignoutResponseDto response = new SignoutResponseDto();
        response.setId(null); // or appropriate value
        response.setUsername(null);
        response.setEmail(null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userName = jwt.getSubject();
        User user = authService.getCurrentUser(userName);
        UserResponseDto userResponseDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userResponseDto);
    }
}
