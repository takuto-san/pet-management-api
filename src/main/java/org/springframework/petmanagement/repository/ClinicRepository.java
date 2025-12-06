package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.Clinic;

public interface ClinicRepository {

    void save(Clinic clinic) throws DataAccessException;

    void delete(Clinic clinic) throws DataAccessException;

    Clinic findById(UUID id) throws DataAccessException;

    Collection<Clinic> findAll() throws DataAccessException;
}
