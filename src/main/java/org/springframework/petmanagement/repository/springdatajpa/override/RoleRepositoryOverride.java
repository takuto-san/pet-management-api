package org.springframework.petmanagement.repository.springdatajpa.override;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.Role;

@Profile("spring-data-jpa")
public interface RoleRepositoryOverride {
	
	void delete(Role Role);

}
