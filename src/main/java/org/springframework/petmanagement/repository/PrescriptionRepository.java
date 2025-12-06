package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.model.PrescriptionCategory;

public interface PrescriptionRepository {

    void save(Prescription prescription) throws DataAccessException;

    void delete(Prescription prescription) throws DataAccessException;

    Prescription findById(UUID id) throws DataAccessException;

    Collection<Prescription> findAll() throws DataAccessException;
    
    Collection<Prescription> findByCategory(PrescriptionCategory category) throws DataAccessException;
}
