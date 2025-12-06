package org.springframework.petmanagement.service;

import org.springframework.petmanagement.model.Prescription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrescriptionService {
    List<Prescription> findAll();
    Optional<Prescription> findById(UUID id);
    Prescription save(Prescription prescription);
    void deletePrescription(UUID id);
}
