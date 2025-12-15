package org.springframework.petmanagement.repository.springdatajpa.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.repository.springdatajpa.override.PetRepositoryOverride;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Profile("spring-data-jpa")
public class SpringDataPetRepositoryImpl implements PetRepositoryOverride {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void delete(Pet pet) {
        if (em.contains(pet)) {
            em.remove(pet);
        } else {
            Pet managedPet = em.find(Pet.class, pet.getId());
            if (managedPet != null) {
                em.remove(managedPet);
            }
        }
    }

    public List<PetType> findPetTypes() {
        return Arrays.asList(PetType.values());
    }
}
