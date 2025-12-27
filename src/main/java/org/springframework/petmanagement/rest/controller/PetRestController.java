package org.springframework.petmanagement.rest.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.PetMapper;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.rest.api.PetsApi;
import org.springframework.petmanagement.rest.dto.PetDto;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.rest.dto.PetPageDto;
import org.springframework.petmanagement.service.PetService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
public class PetRestController implements PetsApi {

    private final PetService petService;
    private final PetMapper petMapper;

    public PetRestController(PetService petService, PetMapper petMapper) {
        this.petService = petService;
        this.petMapper = petMapper;
    }

    @PreAuthorize("hasRole(@roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<PetDto> getPet(UUID petId) {
        return petService.getPet(petId)
            .map(pet -> new ResponseEntity<>(petMapper.toPetDto(pet), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole(@roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<PetPageDto> listPets(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Pet> pets = petService.listPets(pageable);
        return ResponseEntity.ok(petMapper.toPetPageDto(pets));
    }

    @PreAuthorize("hasRole(@roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<PetDto> addPet(PetFieldsDto petFieldsDto) {
        try {
            Pet created = petService.createPet(petFieldsDto);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(
                UriComponentsBuilder.newInstance()
                    .path("/pets/{id}")
                    .buildAndExpand(created.getId())
                    .toUri()
            );
            return new ResponseEntity<>(petMapper.toPetDto(created), headers, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole(@roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<PetDto> updatePet(UUID petId, PetFieldsDto petFieldsDto) {
        try {
            Pet updated = petService.updatePet(petId, petFieldsDto);
            return new ResponseEntity<>(petMapper.toPetDto(updated), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            String msg = ex.getMessage() == null ? "" : ex.getMessage();
            if (msg.contains("pet not found")) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else if (msg.contains("pet type not found")) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole(@roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<Void> deletePet(UUID petId) {
        try {
            petService.deletePet(petId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
