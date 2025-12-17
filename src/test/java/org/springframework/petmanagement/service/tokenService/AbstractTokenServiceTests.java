package org.springframework.petmanagement.service.tokenService;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Test
    void shouldGenerateToken() {
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
                return null;
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
                return "testuser";
            }
        };

        String token = tokenService.generateToken(authentication);

        Assertions.assertThat(token).isNotNull();
        Assertions.assertThat(token).isNotEmpty();

        // Decode the token to verify claims
        Jwt decoded = jwtDecoder.decode(token);
        Assertions.assertThat(decoded.getIssuer()).isEqualTo("petmanagement");
        Assertions.assertThat(decoded.getSubject()).isEqualTo("any token name");
        Assertions.assertThat(decoded.getClaimAsString("userName")).isEqualTo("testuser");
        Assertions.assertThat(decoded.getExpiresAt()).isNotNull();
        boolean isAfter = decoded.getExpiresAt().isAfter(Instant.now());
        assertTrue(isAfter);
    }
}
