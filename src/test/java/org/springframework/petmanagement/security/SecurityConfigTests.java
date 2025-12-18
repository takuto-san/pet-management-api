package org.springframework.petmanagement.security;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootTest
class SecurityConfigTests {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Test
    void contextLoads() {
        assertThat(jwtDecoder).isNotNull();
        assertThat(jwtEncoder).isNotNull();
        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
        assertThat(securityFilterChain).isNotNull();
    }
}
