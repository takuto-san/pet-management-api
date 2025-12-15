package org.springframework.petmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.repository.PetTypeRepository;
import org.springframework.petmanagement.service.PetTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PetTypeServiceImpl implements PetTypeService {

    private final PetTypeRepository petTypeRepository;

    public PetTypeServiceImpl(PetTypeRepository petTypeRepository) {
        this.petTypeRepository = petTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetType> listPetTypes() {
        return new ArrayList<>(petTypeRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetType> getPetType(UUID petTypeId) {
        return Optional.ofNullable(petTypeRepository.findById(petTypeId));
    }

    @Override
    public PetType createPetType(PetType petType) {
        petTypeRepository.save(petType);
        return petType;
    }

    @Override
    public PetType updatePetType(UUID petTypeId, PetType petType) {
        PetType current = petTypeRepository.findById(petTypeId);
        if (current == null) throw new IllegalArgumentException("pet type not found");
        petTypeRepository.save(petType);
        return petType;
    }

    @Override
    public void deletePetType(UUID petTypeId) {
        PetType petType = petTypeRepository.findById(petTypeId);
        if (petType == null) throw new IllegalArgumentException("pet type not found");
        petTypeRepository.delete(petType);
    }
}
