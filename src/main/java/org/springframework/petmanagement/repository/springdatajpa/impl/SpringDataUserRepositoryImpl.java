package org.springframework.petmanagement.repository.springdatajpa.impl;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.springdatajpa.override.UserRepositoryOverride;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

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

    @Override
    public Page<User> findByNameKana(String lastNameKana, String firstNameKana, Pageable pageable) {
        String jpql = "SELECT u FROM User u WHERE " +
                      "(:lastNameKana IS NULL OR u.lastNameKana LIKE :lastNameKanaPattern) AND " +
                      "(:firstNameKana IS NULL OR u.firstNameKana LIKE :firstNameKanaPattern)";

        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setParameter("lastNameKana", lastNameKana);
        query.setParameter("lastNameKanaPattern", lastNameKana != null ? "%" + lastNameKana + "%" : null);
        query.setParameter("firstNameKana", firstNameKana);
        query.setParameter("firstNameKanaPattern", firstNameKana != null ? "%" + firstNameKana + "%" : null);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        java.util.List<User> users = query.getResultList();

        // Count query for total elements
        String countJpql = "SELECT COUNT(u) FROM User u WHERE " +
                           "(:lastNameKana IS NULL OR u.lastNameKana LIKE :lastNameKanaPattern) AND " +
                           "(:firstNameKana IS NULL OR u.firstNameKana LIKE :firstNameKanaPattern)";

        TypedQuery<Long> countQuery = em.createQuery(countJpql, Long.class);
        countQuery.setParameter("lastNameKana", lastNameKana);
        countQuery.setParameter("lastNameKanaPattern", lastNameKana != null ? "%" + lastNameKana + "%" : null);
        countQuery.setParameter("firstNameKana", firstNameKana);
        countQuery.setParameter("firstNameKanaPattern", firstNameKana != null ? "%" + firstNameKana + "%" : null);

        Long total = countQuery.getSingleResult();

        return new PageImpl<>(users, pageable, total);
    }
}
