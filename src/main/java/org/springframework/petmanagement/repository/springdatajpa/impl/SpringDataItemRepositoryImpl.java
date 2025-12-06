package org.springframework.petmanagement.repository.springdatajpa.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.Item;
import org.springframework.petmanagement.repository.springdatajpa.override.ItemRepositoryOverride;

@Profile("spring-data-jpa")
public class SpringDataItemRepositoryImpl implements ItemRepositoryOverride {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void delete(Item item) {
        if (em.contains(item)) {
            em.remove(item);
        } else {
            Item managedItem = em.find(Item.class, item.getId());
            if (managedItem != null) {
                em.remove(managedItem);
            }
        }
    }
}
