package org.springframework.petmanagement.service.impl;

import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.repository.ClinicRepository;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;

    public ClinicServiceImpl(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Clinic> findAll() {
        return new ArrayList<>(clinicRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Clinic> findById(UUID id) {
        return Optional.ofNullable(clinicRepository.findById(id));
    }

    @Override
    public Clinic save(Clinic clinic) {
        clinicRepository.save(clinic);
        return clinic;
    }

    @Override
    public void deleteClinic(UUID id) {
        Clinic clinic = clinicRepository.findById(id);
        if (clinic == null) throw new IllegalArgumentException("clinic not found");
        clinicRepository.delete(clinic);
    }
}
