package org.springframework.petmanagement.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.rest.dto.ClinicFieldsDto;

public interface ClinicService {
    Page<Clinic> listClinics(Pageable pageable);
    Optional<Clinic> getClinic(UUID id);
    Clinic createClinic(ClinicFieldsDto fields);
    Clinic updateClinic(UUID id, ClinicFieldsDto fields);
    void deleteClinic(UUID id);
}
