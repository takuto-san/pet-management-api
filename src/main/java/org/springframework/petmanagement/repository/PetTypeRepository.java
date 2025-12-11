package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.PetType;

public interface PetTypeRepository {

    void save(PetType petType) throws DataAccessException;

    void delete(PetType petType) throws DataAccessException;

    PetType findById(UUID id) throws DataAccessException;

    PetType findByName(String name) throws DataAccessException;

    Collection<PetType> findAll() throws DataAccessException;

}