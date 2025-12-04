package org.springframework.petmanagement.service;

import java.util.Collection;
import java.util.Optional; // Optionalをインポート
import java.util.UUID; // UUIDをインポート

import org.springframework.dao.DataAccessException; // DataAccessExceptionをインポート
import org.springframework.petmanagement.model.User;

public interface UserService {

    void saveUser(User user) throws DataAccessException;

    Optional<User> findUserById(UUID id) throws DataAccessException;

    Optional<User> findUserByUsername(String username) throws DataAccessException;
    
    Collection<User> findAllUsers() throws DataAccessException;
    
    void deleteUser(User user) throws DataAccessException;
}