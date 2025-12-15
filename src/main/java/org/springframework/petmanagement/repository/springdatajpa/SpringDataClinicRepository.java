package org.springframework.petmanagement.repository.springdatajpa;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.repository.ClinicRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.ClinicRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataClinicRepository extends ClinicRepository, PagingAndSortingRepository<Clinic, UUID>, ClinicRepositoryOverride {

    Page<Clinic> findAll(Pageable pageable);
}
