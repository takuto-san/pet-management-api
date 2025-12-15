package org.springframework.petmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.petmanagement.mapper.ClinicMapper;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.repository.ClinicRepository;
import org.springframework.petmanagement.rest.dto.ClinicFieldsDto;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;

    public ClinicServiceImpl(ClinicRepository clinicRepository, ClinicMapper clinicMapper) {
        this.clinicRepository = clinicRepository;
        this.clinicMapper = clinicMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Clinic> listClinics() {
        return new ArrayList<>(clinicRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Clinic> getClinic(UUID id) {
        return Optional.ofNullable(clinicRepository.findById(id));
    }

    @Override
    public Clinic createClinic(ClinicFieldsDto fields) {
        Clinic clinic = clinicMapper.toClinic(fields);
        clinicRepository.save(clinic);
        return clinic;
    }

    @Override
    public Clinic updateClinic(UUID id, ClinicFieldsDto fields) {
        Clinic clinic = clinicRepository.findById(id);
        if (clinic == null) throw new IllegalArgumentException("clinic not found");
        clinicMapper.updateClinicFromFields(fields, clinic);
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
