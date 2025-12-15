package org.springframework.petmanagement.repository.springdatajpa;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.repository.PetRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.PetRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataPetRepository extends PetRepository, PagingAndSortingRepository<Pet, UUID>, PetRepositoryOverride {

    @Override
    List<PetType> findPetTypes() throws DataAccessException;

    Page<Pet> findAll(Pageable pageable);

}
