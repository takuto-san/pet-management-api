package org.springframework.petmanagement.repository.springdatajpa;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.model.type.MedicationType;
import org.springframework.petmanagement.repository.PrescriptionRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.PrescriptionRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataPrescriptionRepository extends PrescriptionRepository, PagingAndSortingRepository<Prescription, UUID>, PrescriptionRepositoryOverride {

    Page<Prescription> findAll(Pageable pageable);

    @Override
    @Query("SELECT p FROM Prescription p WHERE p.category = :category")
    List<Prescription> findByCategory(@Param("category") MedicationType category);
}
