package org.springframework.petmanagement.repository.springdatajpa;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.Repository;
import org.springframework.petmanagement.model.Role;
import org.springframework.petmanagement.repository.RoleRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.RoleRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataRoleRepository extends RoleRepository, Repository<Role, UUID>, RoleRepositoryOverride {

}