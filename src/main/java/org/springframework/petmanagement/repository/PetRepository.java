package org.springframework.petmanagement.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.PetType;

public interface PetRepository {

    List<PetType> findPetTypes() throws DataAccessException;

    Pet findById(UUID id) throws DataAccessException;

    void save(Pet pet) throws DataAccessException;
    
    Collection<Pet> findAll() throws DataAccessException;

    void delete(Pet pet) throws DataAccessException;

}