package org.springframework.petmanagement.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.model.VisitPrescription;
import org.springframework.petmanagement.repository.PrescriptionRepository;
import org.springframework.petmanagement.repository.VisitPrescriptionRepository;
import org.springframework.petmanagement.repository.VisitRepository;
import org.springframework.petmanagement.rest.dto.VisitPrescriptionFieldsDto;
import org.springframework.petmanagement.service.VisitPrescriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VisitPrescriptionServiceImpl implements VisitPrescriptionService {

    private final VisitPrescriptionRepository visitPrescriptionRepository;
    private final VisitRepository visitRepository;
    private final PrescriptionRepository prescriptionRepository;

    public VisitPrescriptionServiceImpl(VisitPrescriptionRepository visitPrescriptionRepository, VisitRepository visitRepository, PrescriptionRepository prescriptionRepository) {
        this.visitPrescriptionRepository = visitPrescriptionRepository;
        this.visitRepository = visitRepository;
        this.prescriptionRepository = prescriptionRepository;
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
        Visit visit = visitRepository.findById(fields.getVisitId());
        if (visit == null) {
            throw new IllegalArgumentException("Visit not found");
        }
        Prescription prescription = prescriptionRepository.findById(fields.getPrescriptionId());
        if (prescription == null) {
            throw new IllegalArgumentException("Prescription not found");
        }

        VisitPrescription vp = new VisitPrescription();
        vp.setVisit(visit);
        vp.setPrescription(prescription);
        vp.setQuantity(fields.getQuantity());
        vp.setUnit(fields.getUnit());
        vp.setDays(fields.getDays());
        vp.setDosageInstructions(fields.getDosageInstructions());
        vp.setPurpose(fields.getPurpose());

        visitPrescriptionRepository.save(vp);
        return vp;
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
