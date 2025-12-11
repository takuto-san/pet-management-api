package org.springframework.petmanagement.repository.springdatajpa.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.repository.springdatajpa.override.VisitRepositoryOverride;

@Profile("spring-data-jpa")
public class SpringDataVisitRepositoryImpl implements VisitRepositoryOverride {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void delete(Visit visit) {
        if (em.contains(visit)) {
            em.remove(visit);
        } else {
            Visit managedVisit = em.find(Visit.class, visit.getId());
            if (managedVisit != null) {
                em.remove(managedVisit);
            }
        }
    }
}
