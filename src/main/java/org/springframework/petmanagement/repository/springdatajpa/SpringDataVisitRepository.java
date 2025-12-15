package org.springframework.petmanagement.repository.springdatajpa;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.repository.VisitRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.VisitRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataVisitRepository extends VisitRepository, PagingAndSortingRepository<Visit, UUID>, VisitRepositoryOverride {

    Page<Visit> findAll(Pageable pageable);

    @Override
    @Query("SELECT v FROM Visit v WHERE v.user.id = :userId ORDER BY v.visitedOn DESC")
    List<Visit> findByUserId(@Param("userId") UUID userId);

    @Override
    @Query("SELECT v FROM Visit v WHERE v.pet.id = :petId ORDER BY v.visitedOn DESC")
    List<Visit> findByPetId(@Param("petId") UUID petId);

    Page<Visit> findByPetId(@Param("petId") UUID petId, Pageable pageable);
}
