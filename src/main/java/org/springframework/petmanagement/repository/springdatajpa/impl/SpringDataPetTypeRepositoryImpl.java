package org.springframework.petmanagement.repository.springdatajpa.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.repository.springdatajpa.override.PetTypeRepositoryOverride;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.UUID;

@Profile("spring-data-jpa")
public class SpringDataPetTypeRepositoryImpl implements PetTypeRepositoryOverride {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void delete(PetType petType) {
        UUID petTypeId = petType.getId();

        Query petDeleteQuery = this.em.createQuery("DELETE FROM Pet pet WHERE pet.type.id = :petTypeId");
        petDeleteQuery.setParameter("petTypeId", petTypeId);
        petDeleteQuery.executeUpdate();

        if (this.em.contains(petType)) {
            this.em.remove(petType);
        } else {
            PetType managedPetType = this.em.find(PetType.class, petTypeId);
            if (managedPetType != null) {
                this.em.remove(managedPetType);
            }
        }
    }
}