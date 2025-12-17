package org.springframework.petmanagement.service.authService;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.rest.dto.JwtResponseDto;
import org.springframework.petmanagement.rest.dto.LoginRequestDto;
import org.springframework.petmanagement.rest.dto.SignupRequestDto;
import org.springframework.petmanagement.service.AuthService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractAuthServiceTests {

    @Autowired
    protected AuthService authService;

    @Test
    void shouldRegisterUser() throws Exception {
        SignupRequestDto signupRequest = new SignupRequestDto("testuser", "test@example.com", "password123");

        authService.registerUser(signupRequest);

        // Verify user can login
        LoginRequestDto loginRequest = new LoginRequestDto("test@example.com", "password123");
        JwtResponseDto response = authService.authenticateUser(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getToken()).isNotNull();
        assertThat(response.getRefreshToken()).isNotNull();
        assertThat(response.getType()).isEqualTo("Bearer");
    }

    @Test
    void shouldAuthenticateUser() throws Exception {
        // First register a user
        SignupRequestDto signupRequest = new SignupRequestDto("authuser", "auth@example.com", "password123");
        authService.registerUser(signupRequest);

        // Then authenticate
        LoginRequestDto loginRequest = new LoginRequestDto("auth@example.com", "password123");
        JwtResponseDto response = authService.authenticateUser(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isNotNull();
        assertThat(response.getRefreshToken()).isNotNull();
        assertThat(response.getType()).isEqualTo("Bearer");
    }

    @Test
    void shouldRefreshToken() throws Exception {
        // Register and authenticate
        SignupRequestDto signupRequest = new SignupRequestDto("refreshuser", "refresh@example.com", "password123");
        authService.registerUser(signupRequest);

        LoginRequestDto loginRequest = new LoginRequestDto("refresh@example.com", "password123");
        JwtResponseDto loginResponse = authService.authenticateUser(loginRequest);

        // Refresh token
        JwtResponseDto refreshResponse = authService.refreshToken(loginResponse.getRefreshToken());

        assertThat(refreshResponse).isNotNull();
        assertThat(refreshResponse.getToken()).isNotNull();
        assertThat(refreshResponse.getRefreshToken()).isNotNull();
        assertThat(refreshResponse.getToken()).isNotEqualTo(loginResponse.getToken()); // New access token
        assertThat(refreshResponse.getRefreshToken()).isNotEqualTo(loginResponse.getRefreshToken()); // New refresh token
    }

    @Test
    void shouldLogoutUser() throws Exception {
        // Register and authenticate
        SignupRequestDto signupRequest = new SignupRequestDto("logoutuser", "logout@example.com", "password123");
        authService.registerUser(signupRequest);

        LoginRequestDto loginRequest = new LoginRequestDto("logout@example.com", "password123");
        authService.authenticateUser(loginRequest);

        // Logout - should not throw exception
        authService.logoutUser("logoutuser");
    }

    private static void set(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}
