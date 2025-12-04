package org.springframework.petmanagement.repository.springdatajpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.Pet;

@Profile("spring-data-jpa")
public class SpringDataPetRepositoryImpl implements PetRepositoryOverride {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void delete(Pet pet) {
        Query visitQuery = this.em.createQuery("DELETE FROM Visit visit WHERE visit.pet.id = :petId");
        visitQuery.setParameter("petId", pet.getId());
        visitQuery.executeUpdate();

        if (em.contains(pet)) {
            em.remove(pet);
        } else {
            Pet managedPet = em.find(Pet.class, pet.getId());
            if (managedPet != null) {
                em.remove(managedPet);
            }
        }
    }
}