package org.springframework.petmanagement.repository.springdatajpa.override;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.Clinic;

@Profile("spring-data-jpa")
public interface ClinicRepositoryOverride {
	
	void delete(Clinic clinic);

}
