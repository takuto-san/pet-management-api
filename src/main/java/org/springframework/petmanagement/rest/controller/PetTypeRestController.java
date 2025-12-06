package org.springframework.petmanagement.rest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.PetTypeMapper;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.rest.api.PettypesApi;
import org.springframework.petmanagement.rest.dto.PetTypeDto;
import org.springframework.petmanagement.rest.dto.PetTypeFieldsDto;
import org.springframework.petmanagement.service.PetTypeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class PetTypeRestController implements PettypesApi {

    private final PetTypeService petTypeService;
    private final PetTypeMapper petTypeMapper;

    public PetTypeRestController(PetTypeService petTypeService, PetTypeMapper petTypeMapper) {
        this.petTypeService = petTypeService;
        this.petTypeMapper = petTypeMapper;
    }

    @PreAuthorize("hasAnyRole(@roles.OWNER_ADMIN, @roles.VET_ADMIN)")
    @Override
    public ResponseEntity<List<PetTypeDto>> listPetTypes() {
        List<PetType> petTypes = petTypeService.findAll();
        List<PetTypeDto> petTypeDtos = new ArrayList<>(petTypeMapper.toPetTypeDtos(petTypes));
        return new ResponseEntity<>(petTypeDtos, HttpStatus.OK); // 空でも200で返す
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @Override
    public ResponseEntity<PetTypeDto> addPetType(@Valid @RequestBody PetTypeFieldsDto petTypeFieldsDto) {
        PetType type = petTypeService.create(petTypeFieldsDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
            UriComponentsBuilder.newInstance()
                .path("/api/pettypes/{id}")
                .buildAndExpand(type.getId())
                .toUri()
        );
        return new ResponseEntity<>(petTypeMapper.toPetTypeDto(type), headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole(@roles.OWNER_ADMIN, @roles.VET_ADMIN)")
    @GetMapping("/pettypes/{petTypeId}")
    public ResponseEntity<PetTypeDto> getPetType(@PathVariable("petTypeId") UUID petTypeId) {
        Optional<PetType> petTypeOpt = petTypeService.findById(petTypeId);
        return petTypeOpt
            .map(p -> new ResponseEntity<>(petTypeMapper.toPetTypeDto(p), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @PutMapping("/pettypes/{petTypeId}")
    public ResponseEntity<PetTypeDto> updatePetType(@PathVariable("petTypeId") UUID petTypeId,
                                                    @Valid @RequestBody PetTypeFieldsDto petTypeFieldsDto) {
        try {
            PetType updated = petTypeService.update(petTypeId, petTypeFieldsDto);
            return new ResponseEntity<>(petTypeMapper.toPetTypeDto(updated), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @DeleteMapping("/pettypes/{petTypeId}")
    public ResponseEntity<Void> deletePetType(@PathVariable("petTypeId") UUID petTypeId) {
        try {
            petTypeService.delete(petTypeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}