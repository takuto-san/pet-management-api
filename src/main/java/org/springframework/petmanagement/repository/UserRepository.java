package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.User;

public interface UserRepository {

    void save(User user) throws DataAccessException;

    void delete(User user) throws DataAccessException;

    User findById(UUID id) throws DataAccessException;

    User findByUsername(String username) throws DataAccessException;

    Collection<User> findAll() throws DataAccessException;
    
}