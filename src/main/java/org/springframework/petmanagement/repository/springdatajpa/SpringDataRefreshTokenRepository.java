package org.springframework.petmanagement.repository.springdatajpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;
import org.springframework.petmanagement.model.RefreshToken;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.RefreshTokenRepository;

@Profile("spring-data-jpa")
public interface SpringDataRefreshTokenRepository extends RefreshTokenRepository, Repository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
