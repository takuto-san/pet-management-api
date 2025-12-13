package org.springframework.petmanagement.service.impl;

import org.springframework.petmanagement.mapper.PetTypeMapper;
import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.repository.PetTypeRepository;
import org.springframework.petmanagement.rest.dto.PetTypeFieldsDto;
import org.springframework.petmanagement.service.PetTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PetTypeServiceImpl implements PetTypeService {

    private final PetTypeRepository petTypeRepository;
    private final PetTypeMapper petTypeMapper;

    public PetTypeServiceImpl(PetTypeRepository petTypeRepository,
                              PetTypeMapper petTypeMapper) {
        this.petTypeRepository = petTypeRepository;
        this.petTypeMapper = petTypeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetType> findAll() {
        return new ArrayList<>(petTypeRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetType> findById(UUID petTypeId) {
        return Optional.ofNullable(petTypeRepository.findById(petTypeId));
    }

    @Override
    public PetType create(PetTypeFieldsDto fields) {
        PetType petType = petTypeMapper.toPetType(fields);
        petTypeRepository.save(petType);
        return petType;
    }

    @Override
    public PetType update(UUID petTypeId, PetTypeFieldsDto fields) {
        PetType current = petTypeRepository.findById(petTypeId);
        if (current == null) throw new IllegalArgumentException("pet type not found");
        petTypeMapper.updatePetTypeFromFields(fields, current);
        petTypeRepository.save(current);
        return current;
    }

    @Override
    public void delete(UUID petTypeId) {
        PetType petType = petTypeRepository.findById(petTypeId);
        if (petType == null) throw new IllegalArgumentException("pet type not found");
        petTypeRepository.delete(petType);
    }
}