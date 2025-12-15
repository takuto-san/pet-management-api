package org.springframework.petmanagement.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.service.PetTypeService;
import org.springframework.stereotype.Service;

@Service
public class PetTypeServiceImpl implements PetTypeService {

    @Override
    public List<PetType> listPetTypes() {
        return Arrays.asList(PetType.values());
    }

    @Override
    public Optional<PetType> getPetType(UUID petTypeId) {
        // For enum, we can't find by UUID, so return empty
        return Optional.empty();
    }

    @Override
    public PetType createPetType(PetType petType) {
        throw new UnsupportedOperationException("PetType is an enum and cannot be created");
    }

    @Override
    public PetType updatePetType(UUID petTypeId, PetType petType) {
        throw new UnsupportedOperationException("PetType is an enum and cannot be updated");
    }

    @Override
    public void deletePetType(UUID petTypeId) {
        throw new UnsupportedOperationException("PetType is an enum and cannot be deleted");
    }
}
