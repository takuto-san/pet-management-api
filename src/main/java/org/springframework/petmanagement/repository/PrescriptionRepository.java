package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.model.type.MedicationType;

public interface PrescriptionRepository {

    void save(Prescription prescription) throws DataAccessException;

    void delete(Prescription prescription) throws DataAccessException;

    Prescription findById(UUID id) throws DataAccessException;

    Collection<Prescription> findAll() throws DataAccessException;

    Collection<Prescription> findByCategory(MedicationType category) throws DataAccessException;

    Page<Prescription> findAll(Pageable pageable) throws DataAccessException;
}
