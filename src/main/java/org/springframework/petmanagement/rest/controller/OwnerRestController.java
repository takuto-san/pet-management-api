package org.springframework.petmanagement.rest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.OwnerMapper;
import org.springframework.petmanagement.mapper.PetMapper;
import org.springframework.petmanagement.model.Owner;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.rest.api.OwnersApi;
import org.springframework.petmanagement.rest.dto.OwnerDto;
import org.springframework.petmanagement.rest.dto.OwnerFieldsDto;
import org.springframework.petmanagement.rest.dto.PetDto;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class OwnerRestController implements OwnersApi {

    private final ClinicService clinicService;

    private final OwnerMapper ownerMapper;

    private final PetMapper petMapper;

    public OwnerRestController(ClinicService clinicService,
                               OwnerMapper ownerMapper,
                               PetMapper petMapper) {
        this.clinicService = clinicService;
        this.ownerMapper = ownerMapper;
        this.petMapper = petMapper;
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<List<OwnerDto>> listOwners(@Nullable String lastNameKana, @Nullable String firstNameKana) {
        Collection<Owner> owners;
        if (lastNameKana != null || firstNameKana != null) {
            owners = this.clinicService.findOwnerByKana(lastNameKana, firstNameKana);
        } else {
            owners = this.clinicService.findAllOwners();
        }
        if (owners.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ownerMapper.toOwnerDtoCollection(owners), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<OwnerDto> getOwner(UUID ownerId) {
        return this.clinicService.findOwnerById(ownerId)
            .map(owner -> new ResponseEntity<>(ownerMapper.toOwnerDto(owner), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<OwnerDto> addOwner(OwnerFieldsDto ownerFieldsDto) {
        HttpHeaders headers = new HttpHeaders();
        Owner owner = ownerMapper.toOwner(ownerFieldsDto);
        
        this.clinicService.saveOwner(owner);
        OwnerDto ownerDto = ownerMapper.toOwnerDto(owner);
        headers.setLocation(UriComponentsBuilder.newInstance()
            .path("/api/owners/{id}").buildAndExpand(owner.getId()).toUri());
        return new ResponseEntity<>(ownerDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<OwnerDto> updateOwner(UUID ownerId, OwnerFieldsDto ownerFieldsDto) {
        Owner currentOwner = this.clinicService.findOwnerById(ownerId).orElse(null);
        if (currentOwner == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Owner updatedOwner = ownerMapper.toOwner(ownerFieldsDto, currentOwner);

        this.clinicService.saveOwner(updatedOwner);
        return new ResponseEntity<>(ownerMapper.toOwnerDto(updatedOwner), HttpStatus.OK);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Transactional
    @Override
    public ResponseEntity<Void> deleteOwner(UUID ownerId) {
        Owner owner = this.clinicService.findOwnerById(ownerId).orElse(null);
        if (owner == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deleteOwner(owner);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<PetDto> addPetToOwner(UUID ownerId, PetFieldsDto petFieldsDto) {
        HttpHeaders headers = new HttpHeaders();
        
        Pet pet = petMapper.toPet(petFieldsDto);
        
        Owner owner = this.clinicService.findOwnerById(ownerId).orElse(null);
        if (owner == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        pet.setOwner(owner);
        
        this.clinicService.savePet(pet);
        
        PetDto petDto = petMapper.toPetDto(pet);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/pets/{id}")
            .buildAndExpand(pet.getId()).toUri());
        return new ResponseEntity<>(petDto, headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<PetDto> updateOwnersPet(UUID ownerId, UUID petId, PetFieldsDto petFieldsDto) {
        Owner currentOwner = this.clinicService.findOwnerById(ownerId).orElse(null);
        if (currentOwner == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        Pet currentPet = this.clinicService.findPetById(petId).orElse(null);
        
        if (currentPet != null && currentPet.getOwner().getId().equals(ownerId)) {
            
            UUID newTypeId = petFieldsDto.getTypeId();
            PetType newPetType = this.clinicService.findPetTypeById(newTypeId).orElse(null);
            if (newPetType == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            currentPet.setType(newPetType);
            
            currentPet.setBirthDate(petFieldsDto.getBirthDate());
            currentPet.setName(petFieldsDto.getName());
            currentPet.setSex(petFieldsDto.getSex());

            this.clinicService.savePet(currentPet);
            return new ResponseEntity<>(petMapper.toPetDto(currentPet), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<PetDto> getOwnersPet(UUID ownerId, UUID petId) {
        Owner owner = this.clinicService.findOwnerById(ownerId).orElse(null);
        if (owner != null) {
            Pet pet = owner.getPet(petId);
            if (pet != null) {
                return new ResponseEntity<>(petMapper.toPetDto(pet), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}