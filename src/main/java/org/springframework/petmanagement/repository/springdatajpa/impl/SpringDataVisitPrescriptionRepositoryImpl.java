package org.springframework.petmanagement.repository.springdatajpa.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.VisitPrescription;
import org.springframework.petmanagement.repository.springdatajpa.override.VisitPrescriptionRepositoryOverride;

@Profile("spring-data-jpa")
public class SpringDataVisitPrescriptionRepositoryImpl implements VisitPrescriptionRepositoryOverride {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void delete(VisitPrescription visitPrescription) {
        if (em.contains(visitPrescription)) {
            em.remove(visitPrescription);
        } else {
            VisitPrescription managedVisitPrescription = em.find(VisitPrescription.class, visitPrescription.getId());
            if (managedVisitPrescription != null) {
                em.remove(managedVisitPrescription);
            }
        }
    }
}
