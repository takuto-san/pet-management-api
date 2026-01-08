package org.springframework.petmanagement.rest.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.PetMapper;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.repository.PetRepository;
import org.springframework.petmanagement.rest.api.PetsApi;
import org.springframework.petmanagement.rest.dto.PetDto;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;
import org.springframework.petmanagement.rest.dto.PetPageDto;
import org.springframework.petmanagement.service.PetService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
public class PetController implements PetsApi {

    private final PetService petService;
    private final PetMapper petMapper;
    private final PetRepository petRepository;

    public PetController(PetService petService, PetMapper petMapper, PetRepository petRepository) {
        this.petService = petService;
        this.petMapper = petMapper;
        this.petRepository = petRepository;
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
