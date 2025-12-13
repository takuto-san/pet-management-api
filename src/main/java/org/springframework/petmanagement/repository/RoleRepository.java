package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.Role;

public interface RoleRepository {

    void save(Role Role) throws DataAccessException;

    void delete(Role Role) throws DataAccessException;

    Role findById(UUID id) throws DataAccessException;

    Role findByName(String name) throws DataAccessException;

    Collection<Role> findAll() throws DataAccessException;

}