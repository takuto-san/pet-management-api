package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.model.Visit;

public interface VisitRepository {

    void save(Visit visit) throws DataAccessException;

    void delete(Visit visit) throws DataAccessException;

    Visit findById(UUID id) throws DataAccessException;

    Collection<Visit> findAll() throws DataAccessException;

    Collection<Visit> findByPetId(UUID petId) throws DataAccessException;

    Page<Visit> findByPetId(UUID petId, Pageable pageable) throws DataAccessException;

    Page<Visit> findAll(Pageable pageable) throws DataAccessException;
}
