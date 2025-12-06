package org.springframework.petmanagement.service;

import org.springframework.petmanagement.model.VisitPrescription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitPrescriptionService {
    List<VisitPrescription> findByVisitId(UUID visitId);
    Optional<VisitPrescription> findById(UUID id);
    VisitPrescription save(VisitPrescription visitPrescription);
    void deleteVisitPrescription(UUID visitId, UUID visitPrescriptionId);
}
