package org.springframework.petmanagement.repository.springdatajpa.override;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.Visit;

@Profile("spring-data-jpa")
public interface VisitRepositoryOverride {
	
	void delete(Visit visit);

}
