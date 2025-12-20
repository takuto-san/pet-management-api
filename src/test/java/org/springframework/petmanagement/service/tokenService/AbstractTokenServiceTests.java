package org.springframework.petmanagement.service.tokenService;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractTokenServiceTests {

    @Autowired
    protected TokenService tokenService;

    @Autowired
    protected JwtDecoder jwtDecoder;

    @Autowired
    protected UserRepository userRepository;

    @Test
    void shouldGenerateToken() {
        User user = User.builder().username("testuser").email("test@example.com").password("password").enabled(true).build();
        final User savedUser = userRepository.save(user);
        // Create a simple Authentication implementation
        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.emptyList();
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return savedUser;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                // No-op
            }

            @Override
            public String getName() {
                return savedUser.getName();
            }
        };

        String token = tokenService.generateToken(authentication);

        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(token).isNotEmpty();

        // Decode the token to verify claims
        Jwt decoded = jwtDecoder.decode(token);
        Assertions.assertThat(decoded.getSubject()).isEqualTo("testuser");
        Assertions.assertThat(decoded.getExpiresAt()).isNotNull();
        boolean isAfter = decoded.getExpiresAt().isAfter(Instant.now());
        assertTrue(isAfter);
    }

    @Test
    void shouldGenerateRefreshToken() {
        User user = User.builder().username("testuser").email("test@example.com").password("password").enabled(true).build();
        final User savedUser = userRepository.save(user);
        // Create a simple Authentication implementation
        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.emptyList();
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return savedUser;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                // No-op
            }

            @Override
            public String getName() {
                return savedUser.getName();
            }
        };

        String refreshToken = tokenService.generateRefreshToken(authentication);

        Assertions.assertThat(refreshToken).isNotNull();
        Assertions.assertThat(refreshToken).isNotEmpty();

        // Decode the token to verify claims
        Jwt decoded = jwtDecoder.decode(refreshToken);
        Assertions.assertThat(decoded.getSubject()).isEqualTo("testuser");
        Assertions.assertThat(decoded.getExpiresAt()).isNotNull();
        boolean isAfter = decoded.getExpiresAt().isAfter(Instant.now());
        assertTrue(isAfter);
    }
}
