package org.springframework.petmanagement.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;

public interface PetService {
    Page<Pet> listPets(Pageable pageable);
    Optional<Pet> getPet(UUID petId);
    Pet createPet(PetFieldsDto fields);
    Pet updatePet(UUID petId, PetFieldsDto fields);
    void deletePet(UUID petId);
    Page<Pet> listPetsByUser(UUID userId, Pageable pageable);
}
