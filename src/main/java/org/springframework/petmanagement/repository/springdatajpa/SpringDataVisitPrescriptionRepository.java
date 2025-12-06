package org.springframework.petmanagement.repository.springdatajpa;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.petmanagement.model.VisitPrescription;
import org.springframework.petmanagement.repository.VisitPrescriptionRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.VisitPrescriptionRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataVisitPrescriptionRepository extends VisitPrescriptionRepository, Repository<VisitPrescription, UUID>, VisitPrescriptionRepositoryOverride {
    
    @Override
    @Query("SELECT vp FROM VisitPrescription vp WHERE vp.visit.id = :visitId")
    List<VisitPrescription> findByVisitId(@Param("visitId") UUID visitId);
}
