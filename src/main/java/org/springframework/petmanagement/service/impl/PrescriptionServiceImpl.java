package org.springframework.petmanagement.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.mapper.PrescriptionMapper;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.repository.PrescriptionRepository;
import org.springframework.petmanagement.rest.dto.PrescriptionFieldsDto;
import org.springframework.petmanagement.service.PrescriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, PrescriptionMapper prescriptionMapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionMapper = prescriptionMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Prescription> listPrescriptions(Pageable pageable) {
        return prescriptionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Prescription> getPrescription(UUID id) {
        return Optional.ofNullable(prescriptionRepository.findById(id));
    }

    @Override
    public Prescription createPrescription(PrescriptionFieldsDto fields) {
        Prescription prescription = prescriptionMapper.toPrescription(fields);
        prescriptionRepository.save(prescription);
        return prescription;
    }

    @Override
    public Prescription updatePrescription(UUID id, PrescriptionFieldsDto fields) {
        Prescription prescription = prescriptionRepository.findById(id);
        if (prescription == null) {
            throw new IllegalArgumentException("Prescription not found: " + id);
        }
        prescriptionMapper.updatePrescriptionFromFields(fields, prescription);
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
