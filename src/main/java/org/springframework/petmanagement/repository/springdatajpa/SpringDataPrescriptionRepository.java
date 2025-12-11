package org.springframework.petmanagement.repository.springdatajpa;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.model.type.MedicationType;
import org.springframework.petmanagement.repository.PrescriptionRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.PrescriptionRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataPrescriptionRepository extends PrescriptionRepository, Repository<Prescription, UUID>, PrescriptionRepositoryOverride {
    
    @Override
    @Query("SELECT p FROM Prescription p WHERE p.category = :category ORDER BY p.name")
    List<Prescription> findByCategory(@Param("category") MedicationType category);
}
