package org.springframework.petmanagement.repository.springdatajpa.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.petmanagement.model.Role;
import org.springframework.petmanagement.repository.springdatajpa.override.RoleRepositoryOverride;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Profile("spring-data-jpa")
public class SpringDataRoleRepositoryImpl implements RoleRepositoryOverride {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void delete(Role role) {
        if (this.em.contains(role)) {
            this.em.remove(role);
        } else {
            Role managedRole = this.em.find(Role.class, role.getId());
            if (managedRole != null) {
                this.em.remove(managedRole);
            }
        }
    }
}
