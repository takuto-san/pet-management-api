package org.springframework.petmanagement.repository.springdatajpa;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.UserRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataUserRepository extends UserRepository, Repository<User, UUID>, UserRepositoryOverride {

    
}