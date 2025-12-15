package org.springframework.petmanagement.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.petmanagement.model.VisitPrescription;
import org.springframework.petmanagement.repository.VisitPrescriptionRepository;
import org.springframework.petmanagement.rest.dto.VisitPrescriptionFieldsDto;
import org.springframework.petmanagement.service.VisitPrescriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VisitPrescriptionServiceImpl implements VisitPrescriptionService {

    private final VisitPrescriptionRepository visitPrescriptionRepository;

    public VisitPrescriptionServiceImpl(VisitPrescriptionRepository visitPrescriptionRepository) {
        this.visitPrescriptionRepository = visitPrescriptionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VisitPrescription> listVisitPrescriptionsByVisitId(UUID visitId) {
        Collection<VisitPrescription> visitPrescriptions = visitPrescriptionRepository.findByVisitId(visitId);
        return new ArrayList<>(visitPrescriptions);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VisitPrescription> getVisitPrescription(UUID id) {
        return Optional.ofNullable(visitPrescriptionRepository.findById(id));
    }

    @Override
    public VisitPrescription createVisitPrescription(VisitPrescriptionFieldsDto fields) {
        // TODO: implement
        return null;
    }

    @Override
    public VisitPrescription updateVisitPrescription(UUID id, VisitPrescriptionFieldsDto fields) {
        // TODO: implement
        return null;
    }

    @Override
    public void deleteVisitPrescription(UUID visitId, UUID visitPrescriptionId) {
        VisitPrescription visitPrescription = visitPrescriptionRepository.findById(visitPrescriptionId);
        if (visitPrescription == null) throw new IllegalArgumentException("visit prescription not found");
        if (!visitPrescription.getVisit().getId().equals(visitId)) {
            throw new IllegalArgumentException("visit prescription does not belong to the specified visit");
        }
        visitPrescriptionRepository.delete(visitPrescription);
    }
}
