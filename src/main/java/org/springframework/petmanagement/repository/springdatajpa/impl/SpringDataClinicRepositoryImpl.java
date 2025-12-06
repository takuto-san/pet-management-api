package org.springframework.petmanagement.repository.springdatajpa.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.repository.springdatajpa.override.ClinicRepositoryOverride;

@Profile("spring-data-jpa")
public class SpringDataClinicRepositoryImpl implements ClinicRepositoryOverride {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void delete(Clinic clinic) {
        if (em.contains(clinic)) {
            em.remove(clinic);
        } else {
            Clinic managedClinic = em.find(Clinic.class, clinic.getId());
            if (managedClinic != null) {
                em.remove(managedClinic);
            }
        }
    }
}
