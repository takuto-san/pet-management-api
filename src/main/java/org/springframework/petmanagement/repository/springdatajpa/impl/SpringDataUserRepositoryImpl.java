package org.springframework.petmanagement.repository.springdatajpa.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.springdatajpa.override.UserRepositoryOverride;

@Profile("spring-data-jpa")
public class SpringDataUserRepositoryImpl implements UserRepositoryOverride {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void delete(User user) {
        if (em.contains(user)) {
            em.remove(user);
        } else {
            User managedUser = em.find(User.class, user.getId());
            if (managedUser != null) {
                em.remove(managedUser);
            }
        }
    }
}