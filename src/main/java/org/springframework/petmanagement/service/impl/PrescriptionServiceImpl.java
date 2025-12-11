package org.springframework.petmanagement.service.impl;

import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.repository.PrescriptionRepository;
import org.springframework.petmanagement.service.PrescriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescription> findAll() {
        return new ArrayList<>(prescriptionRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Prescription> findById(UUID id) {
        return Optional.ofNullable(prescriptionRepository.findById(id));
    }

    @Override
    public Prescription save(Prescription prescription) {
        prescriptionRepository.save(prescription);
        return prescription;
    }

    @Override
    public void deletePrescription(UUID id) {
        Prescription prescription = prescriptionRepository.findById(id);
        if (prescription == null) throw new IllegalArgumentException("prescription not found");
        prescriptionRepository.delete(prescription);
    }
}
