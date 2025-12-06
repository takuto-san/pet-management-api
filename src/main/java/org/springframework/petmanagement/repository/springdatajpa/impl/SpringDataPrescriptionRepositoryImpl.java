package org.springframework.petmanagement.repository.springdatajpa.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.repository.springdatajpa.override.PrescriptionRepositoryOverride;

@Profile("spring-data-jpa")
public class SpringDataPrescriptionRepositoryImpl implements PrescriptionRepositoryOverride {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void delete(Prescription prescription) {
        if (em.contains(prescription)) {
            em.remove(prescription);
        } else {
            Prescription managedPrescription = em.find(Prescription.class, prescription.getId());
            if (managedPrescription != null) {
                em.remove(managedPrescription);
            }
        }
    }
}
