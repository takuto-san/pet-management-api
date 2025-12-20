package org.springframework.petmanagement.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final UserRepository userRepository;

    public TokenService(JwtEncoder encoder, JwtDecoder decoder, UserRepository userRepository) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.userRepository = userRepository;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("petmanagement")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefreshToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("petmanagement")
                .issuedAt(now)
                .expiresAt(now.plus(7, ChronoUnit.DAYS)) // Refresh token expires in 7 days
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Authentication validateRefreshToken(String refreshToken) {
        try {
            Jwt jwt = decoder.decode(refreshToken);
            String username = jwt.getSubject();
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new JwtException("Invalid refresh token"));
            return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                user.getUsername(), null, user.getAuthorities());
        } catch (Exception e) {
            throw new org.springframework.security.oauth2.jwt.JwtException("Invalid refresh token");
        }
    }

    public int getTokenExpirationSeconds() {
        return 3600; // 1 hour
    }
}
