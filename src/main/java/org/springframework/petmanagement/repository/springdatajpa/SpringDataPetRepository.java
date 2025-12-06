package org.springframework.petmanagement.repository.springdatajpa;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.repository.PetRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.PetRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataPetRepository extends PetRepository, Repository<Pet, UUID>, PetRepositoryOverride {

    @Override
    @Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
    List<PetType> findPetTypes() throws DataAccessException;
    
}