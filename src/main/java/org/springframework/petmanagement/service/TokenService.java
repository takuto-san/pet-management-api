package org.springframework.petmanagement.service;

import org.springframework.security.core.Authentication;

public interface TokenService {

    String generateToken(Authentication authentication);

    String generateRefreshToken(Authentication authentication);

    Authentication validateRefreshToken(String refreshToken);

    void deleteRefreshToken(String token);

    int getTokenExpirationSeconds();
}
