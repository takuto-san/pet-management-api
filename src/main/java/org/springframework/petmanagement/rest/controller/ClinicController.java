package org.springframework.petmanagement.rest.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.ClinicMapper;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.rest.api.ClinicsApi;
import org.springframework.petmanagement.rest.dto.ClinicDto;
import org.springframework.petmanagement.rest.dto.ClinicFieldsDto;
import org.springframework.petmanagement.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class ClinicController implements ClinicsApi {

    private final ClinicService clinicService;
    private final ClinicMapper clinicMapper;

    public ClinicController(ClinicService clinicService, ClinicMapper clinicMapper) {
        this.clinicService = clinicService;
        this.clinicMapper = clinicMapper;
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<List<ClinicDto>> listClinics() {
        List<Clinic> clinics = clinicService.findAll();
        return ResponseEntity.ok(clinicMapper.toClinicDtoList(clinics));
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.OWNER_ADMIN)")
    @Override
    public ResponseEntity<ClinicDto> getClinic(UUID clinicId) {
        Optional<Clinic> clinicOpt = clinicService.findById(clinicId);
        return clinicOpt
            .map(clinic -> ResponseEntity.ok(clinicMapper.toClinicDto(clinic)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<ClinicDto> addClinic(ClinicFieldsDto clinicFieldsDto) {
        Clinic clinic = clinicMapper.toClinic(clinicFieldsDto);
        Clinic saved = clinicService.save(clinic);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
            UriComponentsBuilder.newInstance()
                .path("/api/clinics/{id}")
                .buildAndExpand(saved.getId())
                .toUri()
        );
        return new ResponseEntity<>(clinicMapper.toClinicDto(saved), headers, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<ClinicDto> updateClinic(UUID clinicId, ClinicFieldsDto clinicFieldsDto) {
        Optional<Clinic> clinicOpt = clinicService.findById(clinicId);
        if (clinicOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Clinic clinic = clinicOpt.get();
        clinicMapper.updateClinicFromFields(clinicFieldsDto, clinic);
        Clinic updated = clinicService.save(clinic);
        return ResponseEntity.ok(clinicMapper.toClinicDto(updated));
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<Void> deleteClinic(UUID clinicId) {
        try {
            clinicService.deleteClinic(clinicId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
