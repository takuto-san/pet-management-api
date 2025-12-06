package org.springframework.petmanagement.service;

import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PetService {
    List<Pet> findAll();
    Optional<Pet> findById(UUID petId);
    Pet updatePet(UUID petId, PetFieldsDto fields);
    void deletePet(UUID petId);
}