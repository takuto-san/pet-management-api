package org.springframework.petmanagement.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.petmanagement.mapper.PetMapper;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.repository.PetRepository;
import org.springframework.petmanagement.repository.PetTypeRepository;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.service.PetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;
    private final UserRepository userRepository;
    private final PetMapper petMapper;

    public PetServiceImpl(PetRepository petRepository,
                          PetTypeRepository petTypeRepository,
                          UserRepository userRepository,
                          PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petTypeRepository = petTypeRepository;
        this.userRepository = userRepository;
        this.petMapper = petMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pet> findAll() {
        return new ArrayList<>(petRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pet> findById(UUID petId) {
        return Optional.ofNullable(petRepository.findById(petId));
    }

    @Override
    public Pet createPet(PetFieldsDto fields) {
        if (fields.getTypeId() == null) {
            throw new IllegalArgumentException("pet type id is required");
        }
        if (fields.getUserId() == null) {
            throw new IllegalArgumentException("user id is required");
        }
        
        PetType type = petTypeRepository.findById(fields.getTypeId());
        if (type == null) throw new IllegalArgumentException("pet type not found");
        
        User user = userRepository.findById(fields.getUserId());
        if (user == null) throw new IllegalArgumentException("user not found");

        Pet pet = petMapper.toPet(fields);
        pet.setType(type);
        pet.setUser(user);

        petRepository.save(pet);
        return pet;
    }

    @Override
    public Pet updatePet(UUID petId, PetFieldsDto fields) {
        Pet pet = petRepository.findById(petId);
        if (pet == null) throw new IllegalArgumentException("pet not found");

        PetType type = petTypeRepository.findById(fields.getTypeId());
        if (type == null) throw new IllegalArgumentException("pet type not found");

        petMapper.updatePetFromFields(fields, pet);
        pet.setType(type);

        petRepository.save(pet);
        return pet;
    }

    @Override
    public void deletePet(UUID petId) {
        Pet pet = petRepository.findById(petId);
        if (pet == null) throw new IllegalArgumentException("pet not found");
        petRepository.delete(pet);
    }
}