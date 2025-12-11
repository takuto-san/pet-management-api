package org.springframework.petmanagement.service;

import org.springframework.petmanagement.model.Clinic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClinicService {
    List<Clinic> findAll();
    Optional<Clinic> findById(UUID id);
    Clinic save(Clinic clinic);
    void deleteClinic(UUID id);
}
