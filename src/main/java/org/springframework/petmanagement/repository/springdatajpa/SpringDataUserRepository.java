package org.springframework.petmanagement.repository.springdatajpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.UserRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataUserRepository extends UserRepository, PagingAndSortingRepository<User, UUID>, UserRepositoryOverride {

    Page<User> findAll(Pageable pageable);

    Optional<User> findByEmail(String email);

}
