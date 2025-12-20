package org.springframework.petmanagement.service.authService;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.rest.dto.JwtResponseDto;
import org.springframework.petmanagement.rest.dto.SigninRequestDto;
import org.springframework.petmanagement.rest.dto.SignupRequestDto;
import org.springframework.petmanagement.rest.dto.TokenRefreshResponseDto;
import org.springframework.petmanagement.service.AuthService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractAuthServiceTests {

    @Autowired
    protected AuthService authService;

    @Test
    void shouldRegisterUser() throws Exception {
        SignupRequestDto signupRequest = new SignupRequestDto("test@example.com", "Password123");

        authService.registerUser(signupRequest);

        // Verify user can login
        SigninRequestDto loginRequest = new SigninRequestDto("test@example.com", "Password123");
        JwtResponseDto response = authService.authenticateUser(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isNotNull();
        assertThat(response.getAccessToken()).isNotNull();
        assertThat(response.getRefreshToken()).isNotNull();
        assertThat(response.getTokenType()).isEqualTo("Bearer");
    }

    @Test
    void shouldAuthenticateUser() throws Exception {
        // First register a user
        SignupRequestDto signupRequest = new SignupRequestDto("auth@example.com", "Password123");
        authService.registerUser(signupRequest);

        // Then authenticate
        SigninRequestDto loginRequest = new SigninRequestDto("auth@example.com", "Password123");
        JwtResponseDto response = authService.authenticateUser(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isNotNull();
        assertThat(response.getRefreshToken()).isNotNull();
        assertThat(response.getTokenType()).isEqualTo("Bearer");
    }

    @Test
    void shouldRefreshToken() throws Exception {
        // Register and authenticate
        SignupRequestDto signupRequest = new SignupRequestDto("refresh@example.com", "Password123");
        authService.registerUser(signupRequest);

        SigninRequestDto loginRequest = new SigninRequestDto("refresh@example.com", "Password123");
        JwtResponseDto loginResponse = authService.authenticateUser(loginRequest);

        String oldRefreshToken = loginResponse.getRefreshToken();

        // Refresh token
        TokenRefreshResponseDto refreshResponse = authService.refreshToken(oldRefreshToken);

        assertThat(refreshResponse).isNotNull();
        assertThat(refreshResponse.getAccessToken()).isNotNull();
        assertThat(refreshResponse.getRefreshToken()).isNotNull();
        assertThat(refreshResponse.getTokenType()).isEqualTo("Bearer");

        // Old refresh token should be invalidated
        assertThatThrownBy(() -> authService.refreshToken(oldRefreshToken))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Invalid refresh token");
    }

    @Test
    void shouldLogoutUser() throws Exception {
        // Register and authenticate
        SignupRequestDto signupRequest = new SignupRequestDto("logout@example.com", "Password123");
        authService.registerUser(signupRequest);

        SigninRequestDto loginRequest = new SigninRequestDto("logout@example.com", "Password123");
        authService.authenticateUser(loginRequest);

        // Logout - should not throw exception
        authService.logoutUser("logout@example.com");
    }

    @Test
    void shouldThrowExceptionWhenRegisteringDuplicateEmail() throws Exception {
        SignupRequestDto signupRequest = new SignupRequestDto("dup@example.com", "Password123");
        authService.registerUser(signupRequest);

        // Try to register again with same email
        SignupRequestDto duplicateRequest = new SignupRequestDto("dup@example.com", "Password456");

        assertThatThrownBy(() -> authService.registerUser(duplicateRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Email already in use");
    }

    private static void set(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}
