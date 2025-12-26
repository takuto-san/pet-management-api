package org.springframework.petmanagement.repository.springdatajpa;

import java.util.Collection;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.petmanagement.model.Space;
import org.springframework.petmanagement.repository.SpaceRepository;

@Profile("spring-data-jpa")
public interface SpringDataSpaceRepository extends SpaceRepository, CrudRepository<Space, UUID> {

    Collection<Space> findByUserId(UUID userId);
}
