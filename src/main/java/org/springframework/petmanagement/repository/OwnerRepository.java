package org.springframework.petmanagement.repository;
import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.UUID;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.Owner;

public interface OwnerRepository {

    Collection<Owner> findByLastName(String lastName) throws DataAccessException;

    Optional<Owner> findById(UUID id) throws DataAccessException;
    
    void save(Owner owner) throws DataAccessException;
    
    Collection<Owner> findAll() throws DataAccessException;
    
    void delete(Owner owner) throws DataAccessException;

    Collection<Owner> findOwnerByKana(@Nullable String lastNameKana, @Nullable String firstNameKana) throws DataAccessException;

}