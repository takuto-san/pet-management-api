package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.model.Clinic;

public interface ClinicRepository {

    void save(Clinic clinic) throws DataAccessException;

    void delete(Clinic clinic) throws DataAccessException;

    Clinic findById(UUID id) throws DataAccessException;

    Collection<Clinic> findAll() throws DataAccessException;

    Page<Clinic> findAll(Pageable pageable) throws DataAccessException;
}
