package org.springframework.petmanagement.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class RefreshTokenTests {

    @Test
    void shouldCreateValidRefreshToken() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .username("testuser")
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test@example.com")
            .build();

        String token = "refresh-token-string";
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(7);

        RefreshToken refreshToken = new RefreshToken(user, token, expiryDate);

        assertThat(refreshToken.getUser()).isEqualTo(user);
        assertThat(refreshToken.getToken()).isEqualTo(token);
        assertThat(refreshToken.getExpiryDate()).isEqualTo(expiryDate);
        assertThat(refreshToken.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldDetectExpiredToken() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .username("testuser")
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test@example.com")
            .build();

        String token = "expired-token";
        LocalDateTime pastExpiryDate = LocalDateTime.now().minusDays(1);

        RefreshToken refreshToken = new RefreshToken(user, token, pastExpiryDate);

        assertThat(refreshToken.isExpired()).isTrue();
    }

    @Test
    void shouldDetectNonExpiredToken() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .username("testuser")
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test@example.com")
            .build();

        String token = "valid-token";
        LocalDateTime futureExpiryDate = LocalDateTime.now().plusDays(1);

        RefreshToken refreshToken = new RefreshToken(user, token, futureExpiryDate);

        assertThat(refreshToken.isExpired()).isFalse();
    }

    @Test
    void shouldSupportBuilderPattern() {
        User user = User.builder()
            .id(UUID.randomUUID())
            .username("testuser")
            .password("password")
            .firstName("太郎")
            .lastName("山田")
            .firstNameKana("タロウ")
            .lastNameKana("ヤマダ")
            .email("test@example.com")
            .build();

        RefreshToken refreshToken = RefreshToken.builder()
            .id(UUID.randomUUID())
            .user(user)
            .token("builder-token")
            .expiryDate(LocalDateTime.now().plusHours(1))
            .createdAt(LocalDateTime.now())
            .build();

        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.getToken()).isEqualTo("builder-token");
    }
}
