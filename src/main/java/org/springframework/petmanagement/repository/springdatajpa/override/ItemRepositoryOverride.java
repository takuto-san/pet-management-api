package org.springframework.petmanagement.repository.springdatajpa.override;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.Item;

@Profile("spring-data-jpa")
public interface ItemRepositoryOverride {
	
	void delete(Item item);

}
