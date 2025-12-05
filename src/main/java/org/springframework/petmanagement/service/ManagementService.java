package org.springframework.petmanagement.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.lang.Nullable;
import org.springframework.petmanagement.model.Owner;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.model.User;

public interface ManagementService {

    Optional<Pet> findPetById(UUID id) throws DataAccessException;
    Collection<Pet> findAllPets() throws DataAccessException;
    void savePet(Pet pet) throws DataAccessException;
    void deletePet(Pet pet) throws DataAccessException;

    Optional<Owner> findOwnerById(UUID id) throws DataAccessException;
    Collection<Owner> findAllOwners() throws DataAccessException;
    void saveOwner(Owner owner) throws DataAccessException;
    void deleteOwner(Owner owner) throws DataAccessException;
    Collection<Owner> findOwnerByKana(@Nullable String lastNameKana, @Nullable String firstNameKana) throws DataAccessException;

    Optional<PetType> findPetTypeById(UUID id) throws DataAccessException; 
    Collection<PetType> findAllPetTypes() throws DataAccessException; 
    void savePetType(PetType petType) throws DataAccessException;
    void deletePetType(PetType petType) throws DataAccessException;

    Optional<User> findUserById(UUID id) throws DataAccessException;
    void saveUser(User user) throws DataAccessException;
    void deleteUser(User user) throws DataAccessException;
}