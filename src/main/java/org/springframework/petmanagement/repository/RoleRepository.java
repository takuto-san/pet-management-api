package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.Role;
import org.springframework.petmanagement.model.type.RoleType;

public interface RoleRepository {

    void save(Role Role) throws DataAccessException;

    void delete(Role Role) throws DataAccessException;

    Role findById(UUID id) throws DataAccessException;

    Optional<Role> findByName(RoleType name);

    Collection<Role> findAll() throws DataAccessException;

}
