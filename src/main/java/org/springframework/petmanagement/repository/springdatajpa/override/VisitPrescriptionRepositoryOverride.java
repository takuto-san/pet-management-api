package org.springframework.petmanagement.repository.springdatajpa.override;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.VisitPrescription;

@Profile("spring-data-jpa")
public interface VisitPrescriptionRepositoryOverride {
	
	void delete(VisitPrescription visitPrescription);

}
