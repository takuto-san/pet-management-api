package org.springframework.petmanagement.service.impl;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.petmanagement.mapper.PetMapper;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.repository.PetRepository;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.service.PetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetMapper petMapper;

    public PetServiceImpl(PetRepository petRepository,
                          UserRepository userRepository,
                          PetMapper petMapper) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.petMapper = petMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pet> listPets(Pageable pageable) {
        return petRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pet> getPet(UUID petId) {
        return Optional.ofNullable(petRepository.findById(petId));
    }

    @Override
    public Pet createPet(PetFieldsDto fields) {
        if (fields.getType() == null) {
            throw new IllegalArgumentException("Pet type is required");
        }
        if (fields.getUserId() == null) {
            throw new IllegalArgumentException("User id is required");
        }

        PetType type = Arrays.stream(PetType.values())
                .filter(pt -> pt.name().equalsIgnoreCase(fields.getType().getValue()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pet type not found: " + fields.getType().getValue()));

        User user = userRepository.findById(fields.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + fields.getUserId()));

        Pet pet = petMapper.toPet(fields);
        pet.setType(type);
        pet.setUser(user);

        petRepository.save(pet);
        return pet;
    }

    @Override
    public Pet updatePet(UUID petId, PetFieldsDto fields) {
        Pet pet = petRepository.findById(petId);
        if (pet == null) {
            throw new IllegalArgumentException("Pet not found: " + petId);
        }

        PetType type = Arrays.stream(PetType.values())
                .filter(pt -> pt.name().equalsIgnoreCase(fields.getType().getValue()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Pet type not found: " + fields.getType().getValue()));

        petMapper.updatePetFromFields(fields, pet);
        pet.setType(type);

        petRepository.save(pet);
        return pet;
    }

    @Override
    public void deletePet(UUID petId) {
        Pet pet = petRepository.findById(petId);
        if (pet == null) {
            throw new IllegalArgumentException("Pet not found: " + petId);
        }

        petRepository.delete(pet);
    }
}
