package org.springframework.petmanagement.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.petmanagement.model.RefreshToken;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.RefreshTokenRepository;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenServiceImpl implements TokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenServiceImpl(JwtEncoder encoder, JwtDecoder decoder, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("petmanagement")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("jti", UUID.randomUUID().toString())
                .claim("userId", user.getId().toString())
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    @Transactional
    public String generateRefreshToken(Authentication authentication) {
        Instant now = Instant.now();

        String scope = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));

        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("petmanagement")
                .issuedAt(now)
                .expiresAt(now.plus(7, ChronoUnit.DAYS)) // Refresh token expires in 7 days
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("jti", UUID.randomUUID().toString())
                .claim("userId", user.getId().toString())
                .build();

        String tokenValue = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        RefreshToken refreshToken = new RefreshToken(user, tokenValue, LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(refreshToken);

        return tokenValue;
    }

    @Override
    @Transactional
    public Authentication validateRefreshToken(String refreshToken) {
        try {
            RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new JwtException("Invalid refresh token"));
            if (storedToken.isExpired()) {
                refreshTokenRepository.delete(storedToken);
                throw new JwtException("Refresh token expired");
            }
            Jwt jwt = decoder.decode(refreshToken);
            User user = storedToken.getUser();
            return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                user.getEmail(), null, user.getAuthorities());
        } catch (Exception e) {
            throw new org.springframework.security.oauth2.jwt.JwtException("Invalid refresh token");
        }
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    @Override
    public int getTokenExpirationSeconds() {
        return 3600; // 1 hour
    }
}
