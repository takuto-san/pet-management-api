package org.springframework.petmanagement.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.PetMapper;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.rest.api.PetsApi;
import org.springframework.petmanagement.rest.dto.PetDto;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.service.ManagementService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Collection;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api")
public class PetRestController implements PetsApi {

    private final ManagementService managementService;
    private final PetMapper petMapper;
    
    public PetRestController(ManagementService managementService, PetMapper petMapper) {
        this.managementService = managementService;
        this.petMapper = petMapper;
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<PetDto> getPet(UUID petId) {
        return this.managementService.findPetById(petId)
            .map(pet -> new ResponseEntity<>(petMapper.toPetDto(pet), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<List<PetDto>> listPets() {
        Collection<Pet> pets = this.managementService.findAllPets();
        
        if (pets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<PetDto> petDtos = new ArrayList<>(petMapper.toPetsDto(pets));
        return new ResponseEntity<>(petDtos, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<PetDto> updatePet(UUID petId, PetFieldsDto petFieldsDto) {
        Pet currentPet = this.managementService.findPetById(petId).orElse(null);
        if (currentPet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        petMapper.updatePetFromFields(petFieldsDto, currentPet);
        
        UUID newTypeId = petFieldsDto.getTypeId();
        PetType newPetType = this.managementService.findPetTypeById(newTypeId).orElse(null); 
        if (newPetType == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        currentPet.setType(newPetType);
        
        this.managementService.savePet(currentPet);
        return new ResponseEntity<>(petMapper.toPetDto(currentPet), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<Void> deletePet(UUID petId) {
        Pet pet = this.managementService.findPetById(petId).orElse(null);
        if (pet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.managementService.deletePet(pet);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}