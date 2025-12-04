package org.springframework.petmanagement.repository.springdatajpa;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.petmanagement.model.Owner;
import org.springframework.petmanagement.repository.OwnerRepository;
import org.springframework.lang.Nullable;

@Profile("spring-data-jpa")
public interface SpringDataOwnerRepository extends OwnerRepository, Repository<Owner, UUID> {

    @Override
    @Query("SELECT DISTINCT owner FROM Owner owner left join fetch owner.pets WHERE owner.lastName LIKE :lastName%")
    Collection<Owner> findByLastName(@Param("lastName") String lastName);

    @Override
    @Query("SELECT owner FROM Owner owner left join fetch owner.pets WHERE owner.id =:id")
    Optional<Owner> findById(@Param("id") UUID id);
    
    @Override
    @Query("SELECT DISTINCT owner FROM Owner owner " +
           "WHERE (:lastNameKana IS NULL OR owner.lastNameKana LIKE %:lastNameKana%) " +
           "AND (:firstNameKana IS NULL OR owner.firstNameKana LIKE %:firstNameKana%) " +
           "ORDER BY owner.lastNameKana, owner.firstNameKana")
    Collection<Owner> findOwnerByKana(
        @Param("lastNameKana") @Nullable String lastNameKana,
        @Param("firstNameKana") @Nullable String firstNameKana
    );
}