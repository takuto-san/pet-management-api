package org.springframework.petmanagement.repository.springdatajpa;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.User;
import org.springframework.dao.DataAccessException;

@Profile("spring-data-jpa")
public interface UserRepositoryOverride {
    
    void delete(User user) throws DataAccessException;

}