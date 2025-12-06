package org.springframework.petmanagement.repository.springdatajpa.override;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.Prescription;

@Profile("spring-data-jpa")
public interface PrescriptionRepositoryOverride {
	
	void delete(Prescription prescription);

}
