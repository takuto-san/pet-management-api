package org.springframework.petmanagement.repository.springdatajpa.override;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.Pet;

@Profile("spring-data-jpa")
public interface PetRepositoryOverride {
	
	void delete(Pet pet);

}
