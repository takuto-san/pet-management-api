package org.springframework.petmanagement.repository;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.RefreshToken;
import org.springframework.petmanagement.model.User;

public interface RefreshTokenRepository {

    RefreshToken save(RefreshToken refreshToken) throws DataAccessException;

    void delete(RefreshToken refreshToken) throws DataAccessException;

    Optional<RefreshToken> findByToken(String token) throws DataAccessException;

    void deleteByUser(User user) throws DataAccessException;
}
