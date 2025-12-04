package org.springframework.petmanagement.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.User;

public interface UserRepository {

    void save(User user) throws DataAccessException;
}
