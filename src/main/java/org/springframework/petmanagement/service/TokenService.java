package org.springframework.petmanagement.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.petmanagement.model.RefreshToken;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.RefreshTokenRepository;
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
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenService(JwtEncoder encoder, JwtDecoder decoder, UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
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

    @Transactional
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

        String tokenValue = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        User user = (User) authentication.getPrincipal();
        RefreshToken refreshToken = new RefreshToken(user, tokenValue, LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(refreshToken);

        return tokenValue;
    }

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
                user, null, user.getAuthorities());
        } catch (Exception e) {
            throw new org.springframework.security.oauth2.jwt.JwtException("Invalid refresh token");
        }
    }

    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

    public int getTokenExpirationSeconds() {
        return 3600; // 1 hour
    }
}
