package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.Space;

public interface SpaceRepository {

    Space save(Space space) throws DataAccessException;

    void delete(Space space) throws DataAccessException;

    Optional<Space> findById(UUID id) throws DataAccessException;

    Collection<Space> findAll() throws DataAccessException;

    Collection<Space> findByUserId(UUID userId) throws DataAccessException;
}
