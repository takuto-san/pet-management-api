package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.User;

public interface UserRepository {

    User save(User user) throws DataAccessException;

    void delete(User user) throws DataAccessException;

    Optional<User> findById(UUID id) throws DataAccessException;

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Collection<User> findAll() throws DataAccessException;
    
}
