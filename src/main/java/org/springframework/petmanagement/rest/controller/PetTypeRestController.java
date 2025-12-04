package org.springframework.petmanagement.rest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.PetTypeMapper;
import org.springframework.petmanagement.model.PetType;
import org.springframework.petmanagement.rest.api.PettypesApi;
import org.springframework.petmanagement.rest.dto.PetTypeDto;
import org.springframework.petmanagement.rest.dto.PetTypeFieldsDto;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.Collection;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api")
public class PetTypeRestController implements PettypesApi {

    private final ClinicService clinicService;
    private final PetTypeMapper petTypeMapper;


    public PetTypeRestController(ClinicService clinicService, PetTypeMapper petTypeMapper) {
        this.clinicService = clinicService;
        this.petTypeMapper = petTypeMapper;
    }

    @PreAuthorize("hasAnyRole(@roles.OWNER_ADMIN, @roles.VET_ADMIN)")
    @Override
    public ResponseEntity<List<PetTypeDto>> listPetTypes() {
        Collection<PetType> petTypes = this.clinicService.findAllPetTypes();
        if (petTypes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(petTypeMapper.toPetTypeDtos(petTypes), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole(@roles.OWNER_ADMIN, @roles.VET_ADMIN)")
    @GetMapping("/pettypes/{petTypeId}")
    public ResponseEntity<PetTypeDto> getPetType(@PathVariable("petTypeId") UUID petTypeId) {
        return this.clinicService.findPetTypeById(petTypeId)
            .map(petType -> new ResponseEntity<>(petTypeMapper.toPetTypeDto(petType), HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @Override
    public ResponseEntity<PetTypeDto> addPetType(PetTypeFieldsDto petTypeFieldsDto) {
        HttpHeaders headers = new HttpHeaders();
        final PetType type = petTypeMapper.toPetType(petTypeFieldsDto);
        this.clinicService.savePetType(type);
        headers.setLocation(UriComponentsBuilder.newInstance().path("/api/pettypes/{id}").buildAndExpand(type.getId()).toUri());
        return new ResponseEntity<>(petTypeMapper.toPetTypeDto(type), headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @PutMapping("/pettypes/{petTypeId}")
    public ResponseEntity<PetTypeDto> updatePetType(@PathVariable("petTypeId") UUID petTypeId, @RequestBody PetTypeFieldsDto petTypeFieldsDto) {
        PetType currentPetType = this.clinicService.findPetTypeById(petTypeId).orElse(null);
        if (currentPetType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        currentPetType.setName(petTypeFieldsDto.getName());
        this.clinicService.savePetType(currentPetType);
        
        return new ResponseEntity<>(petTypeMapper.toPetTypeDto(currentPetType), HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole(@roles.VET_ADMIN)")
    @Transactional
    @DeleteMapping("/pettypes/{petTypeId}")
    public ResponseEntity<Void> deletePetType(@PathVariable("petTypeId") UUID petTypeId) {
        PetType petType = this.clinicService.findPetTypeById(petTypeId).orElse(null);
        if (petType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deletePetType(petType);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}