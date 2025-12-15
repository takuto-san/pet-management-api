package org.springframework.petmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;

public interface PetService {
    List<Pet> listPets();
    Optional<Pet> getPet(UUID petId);
    Pet createPet(PetFieldsDto fields);
    Pet updatePet(UUID petId, PetFieldsDto fields);
    void deletePet(UUID petId);
}
