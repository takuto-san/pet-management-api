package org.springframework.petmanagement.repository.springdatajpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.petmanagement.model.Owner;
import org.springframework.petmanagement.repository.OwnerRepository;

@Profile("spring-data-jpa")
public interface SpringDataOwnerRepository extends OwnerRepository {

    @Override
    @Query("SELECT DISTINCT owner FROM Owner owner left join fetch owner.pets WHERE owner.lastName LIKE :lastName")
    List<Owner> findByLastName(@Param("lastName") String lastName);

    @Override
    @Query("SELECT owner FROM Owner owner left join fetch owner.pets WHERE owner.id = :id")
    @NonNull
    Optional<Owner> findById(@NonNull @Param("id") UUID id);
    
    @Override
    @Query("SELECT DISTINCT owner FROM Owner owner " +
           "WHERE (:lastNameKana IS NULL OR owner.lastNameKana LIKE :lastNameKana) " +
           "AND (:firstNameKana IS NULL OR owner.firstNameKana LIKE :firstNameKana)")
    List<Owner> findByLastNameKanaAndFirstNameKana(
        @Param("lastNameKana") @Nullable String lastNameKana,
        @Param("firstNameKana") @Nullable String firstNameKana
    );
}