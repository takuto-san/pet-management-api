package org.springframework.petmanagement.repository.springdatajpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.petmanagement.model.Role;
import org.springframework.petmanagement.model.type.RoleType;
import org.springframework.petmanagement.repository.RoleRepository;
import org.springframework.petmanagement.repository.springdatajpa.override.RoleRepositoryOverride;

@Profile("spring-data-jpa")
public interface SpringDataRoleRepository extends RoleRepository, Repository<Role, UUID>, RoleRepositoryOverride {

    @Override
    @Query(value = "SELECT * FROM roles WHERE name = CAST(:#{#name.name().toLowerCase()} AS role_type)",  // RoleType（Enum）を小文字の文字列に変換
           nativeQuery = true)
    Optional<Role> findByName(@Param("name") RoleType name);
    
}