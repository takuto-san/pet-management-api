package org.springframework.petmanagement.repository.springdatajpa.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.repository.springdatajpa.override.PetTypeRepositoryOverride;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Profile("spring-data-jpa")
public class SpringDataPetTypeRepositoryImpl implements PetTypeRepositoryOverride {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void delete(PetType petType) {
        String petTypeName = petType.name();

        Query petDeleteQuery = this.em.createQuery("DELETE FROM Pet pet WHERE pet.type = :petType");
        petDeleteQuery.setParameter("petType", petType);
        petDeleteQuery.executeUpdate();

        // PetType is enum, not entity, so no delete needed
    }
}
