package org.springframework.petmanagement.repository.springdatajpa;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.repository.PetTypeRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.PetTypeRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataPetTypeRepository extends PetTypeRepository, Repository<PetType, UUID>, PetTypeRepositoryOverride {

}