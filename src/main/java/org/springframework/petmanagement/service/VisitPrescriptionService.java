package org.springframework.petmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.petmanagement.model.VisitPrescription;
import org.springframework.petmanagement.rest.dto.VisitPrescriptionFieldsDto;

public interface VisitPrescriptionService {
    List<VisitPrescription> listVisitPrescriptionsByVisitId(UUID visitId);
    Optional<VisitPrescription> getVisitPrescription(UUID id);
    VisitPrescription createVisitPrescription(VisitPrescriptionFieldsDto fields);
    VisitPrescription updateVisitPrescription(UUID id, VisitPrescriptionFieldsDto fields);
    void deleteVisitPrescription(UUID visitId, UUID visitPrescriptionId);
}
