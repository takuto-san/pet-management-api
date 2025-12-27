package org.springframework.petmanagement.rest.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.petmanagement.mapper.VisitMapper;
import org.springframework.petmanagement.mapper.VisitPrescriptionMapper;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.model.VisitPrescription;
import org.springframework.petmanagement.repository.PrescriptionRepository;
import org.springframework.petmanagement.rest.api.VisitsApi;
import org.springframework.petmanagement.rest.dto.VisitDto;
import org.springframework.petmanagement.rest.dto.VisitFieldsDto;
import org.springframework.petmanagement.rest.dto.VisitPageDto;
import org.springframework.petmanagement.rest.dto.VisitPrescriptionDto;
import org.springframework.petmanagement.rest.dto.VisitPrescriptionFieldsDto;
import org.springframework.petmanagement.service.VisitPrescriptionService;
import org.springframework.petmanagement.service.VisitService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
public class VisitController implements VisitsApi {

    private final VisitService visitService;
    private final VisitMapper visitMapper;
    private final VisitPrescriptionService visitPrescriptionService;
    private final VisitPrescriptionMapper visitPrescriptionMapper;
    private final PrescriptionRepository prescriptionRepository;

    public VisitController(VisitService visitService,
                          VisitMapper visitMapper,
                          VisitPrescriptionService visitPrescriptionService,
                          VisitPrescriptionMapper visitPrescriptionMapper,
                          PrescriptionRepository prescriptionRepository) {
        this.visitService = visitService;
        this.visitMapper = visitMapper;
        this.visitPrescriptionService = visitPrescriptionService;
        this.visitPrescriptionMapper = visitPrescriptionMapper;
        this.prescriptionRepository = prescriptionRepository;
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<VisitPageDto> listVisits(Integer page, Integer size, @Nullable UUID petId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Visit> visits = visitService.listVisits(petId, pageable);
        return ResponseEntity.ok(visitMapper.toVisitPageDto(visits));
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<VisitDto> getVisit(UUID visitId) {
        Optional<Visit> visitOpt = visitService.getVisit(visitId);
        return visitOpt
            .map(visit -> ResponseEntity.ok(visitMapper.toVisitDto(visit)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<VisitDto> addVisit(VisitFieldsDto visitFieldsDto) {
        try {
            Visit saved = visitService.createVisit(visitFieldsDto);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(
                UriComponentsBuilder.newInstance()
                    .path("/visits/{id}")
                    .buildAndExpand(saved.getId())
                    .toUri()
            );
            return new ResponseEntity<>(visitMapper.toVisitDto(saved), headers, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<VisitDto> updateVisit(UUID visitId, VisitFieldsDto visitFieldsDto) {
        try {
            Visit updated = visitService.updateVisit(visitId, visitFieldsDto);
            return ResponseEntity.ok(visitMapper.toVisitDto(updated));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<Void> deleteVisit(UUID visitId) {
        try {
            visitService.deleteVisit(visitId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<List<VisitPrescriptionDto>> listVisitPrescriptions(UUID visitId) {
        List<VisitPrescription> visitPrescriptions = visitPrescriptionService.listVisitPrescriptionsByVisitId(visitId);
        return ResponseEntity.ok(visitPrescriptionMapper.toVisitPrescriptionDtoList(visitPrescriptions));
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<VisitPrescriptionDto> addVisitPrescription(UUID visitId, VisitPrescriptionFieldsDto visitPrescriptionFieldsDto) {
        try {
            visitPrescriptionFieldsDto.setVisitId(visitId);
            VisitPrescription saved = visitPrescriptionService.createVisitPrescription(visitPrescriptionFieldsDto);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(
                UriComponentsBuilder.newInstance()
                    .path("/visits/{visitId}/prescriptions/{id}")
                    .buildAndExpand(visitId, saved.getId())
                    .toUri()
            );
            return new ResponseEntity<>(visitPrescriptionMapper.toVisitPrescriptionDto(saved), headers, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole(@roles.ADMIN)")
    @Override
    public ResponseEntity<Void> deleteVisitPrescription(UUID visitId, UUID visitPrescriptionId) {
        try {
            visitPrescriptionService.deleteVisitPrescription(visitId, visitPrescriptionId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
