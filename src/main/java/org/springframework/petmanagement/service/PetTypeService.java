package org.springframework.petmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.rest.dto.PetTypeFieldsDto;

public interface PetTypeService {
    List<PetType> findAll();
    Optional<PetType> findById(UUID petTypeId);
    PetType create(PetTypeFieldsDto fields);
    PetType update(UUID petTypeId, PetTypeFieldsDto fields);
    void delete(UUID petTypeId);
}