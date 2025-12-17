package org.springframework.petmanagement.rest.controller;

import org.springframework.petmanagement.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public String createToken(Authentication authentication) {
        String token = tokenService.generateToken(authentication);
        return token;
    }
}
