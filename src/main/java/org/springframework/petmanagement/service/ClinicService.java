package org.springframework.petmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.rest.dto.ClinicFieldsDto;

public interface ClinicService {
    List<Clinic> listClinics();
    Optional<Clinic> getClinic(UUID id);
    Clinic createClinic(ClinicFieldsDto fields);
    Clinic updateClinic(UUID id, ClinicFieldsDto fields);
    void deleteClinic(UUID id);
}
