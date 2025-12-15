package org.springframework.petmanagement.rest.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.PrescriptionMapper;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.rest.api.PrescriptionsApi;
import org.springframework.petmanagement.rest.dto.PrescriptionDto;
import org.springframework.petmanagement.rest.dto.PrescriptionFieldsDto;
import org.springframework.petmanagement.service.PrescriptionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class PrescriptionController implements PrescriptionsApi {

    private final PrescriptionService prescriptionService;
    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionController(PrescriptionService prescriptionService, PrescriptionMapper prescriptionMapper) {
        this.prescriptionService = prescriptionService;
        this.prescriptionMapper = prescriptionMapper;
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<List<PrescriptionDto>> listPrescriptions() {
        List<Prescription> prescriptions = prescriptionService.listPrescriptions();
        return ResponseEntity.ok(prescriptionMapper.toPrescriptionDtoList(prescriptions));
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<PrescriptionDto> addPrescription(PrescriptionFieldsDto prescriptionFieldsDto) {
        Prescription saved = prescriptionService.createPrescription(prescriptionFieldsDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
            UriComponentsBuilder.newInstance()
                .path("/api/prescriptions/{id}")
                .buildAndExpand(saved.getId())
                .toUri()
        );
        return new ResponseEntity<>(prescriptionMapper.toPrescriptionDto(saved), headers, HttpStatus.CREATED);
    }
}
