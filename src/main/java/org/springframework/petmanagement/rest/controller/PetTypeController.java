package org.springframework.petmanagement.rest.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.rest.api.PettypesApi;
import org.springframework.petmanagement.rest.dto.PetTypeDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class PetTypeController implements PettypesApi {

    @Override
    public ResponseEntity<List<PetTypeDto>> listPetTypes() {
        List<PetTypeDto> petTypes = Arrays.asList(PetTypeDto.values());
        return new ResponseEntity<>(petTypes, HttpStatus.OK);
    }
}
