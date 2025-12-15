package org.springframework.petmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.rest.dto.PrescriptionFieldsDto;

public interface PrescriptionService {
    List<Prescription> listPrescriptions();
    Optional<Prescription> getPrescription(UUID id);
    Prescription createPrescription(PrescriptionFieldsDto fields);
    Prescription updatePrescription(UUID id, PrescriptionFieldsDto fields);
    void deletePrescription(UUID id);
}
