package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.VisitPrescription;

public interface VisitPrescriptionRepository {

    void save(VisitPrescription visitPrescription) throws DataAccessException;

    void delete(VisitPrescription visitPrescription) throws DataAccessException;

    VisitPrescription findById(UUID id) throws DataAccessException;

    Collection<VisitPrescription> findAll() throws DataAccessException;
    
    Collection<VisitPrescription> findByVisitId(UUID visitId) throws DataAccessException;
}
