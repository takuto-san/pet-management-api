package org.springframework.petmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.petmanagement.model.type.PetType;

public interface PetTypeService {
    List<PetType> listPetTypes();
    Optional<PetType> getPetType(UUID petTypeId);
    PetType createPetType(PetType petType);
    PetType updatePetType(UUID petTypeId, PetType petType);
    void deletePetType(UUID petTypeId);
}
