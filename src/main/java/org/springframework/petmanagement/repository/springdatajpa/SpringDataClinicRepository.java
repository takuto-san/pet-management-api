package org.springframework.petmanagement.repository.springdatajpa;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.repository.ClinicRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.ClinicRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataClinicRepository extends ClinicRepository, Repository<Clinic, UUID>, ClinicRepositoryOverride {
}
