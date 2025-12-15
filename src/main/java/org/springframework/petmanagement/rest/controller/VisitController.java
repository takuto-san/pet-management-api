package org.springframework.petmanagement.rest.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.petmanagement.mapper.VisitMapper;
import org.springframework.petmanagement.mapper.VisitPrescriptionMapper;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.model.User;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.model.VisitPrescription;
import org.springframework.petmanagement.repository.ClinicRepository;
import org.springframework.petmanagement.repository.PetRepository;
import org.springframework.petmanagement.repository.PrescriptionRepository;
import org.springframework.petmanagement.repository.UserRepository;
import org.springframework.petmanagement.rest.api.VisitsApi;
import org.springframework.petmanagement.rest.dto.VisitDto;
import org.springframework.petmanagement.rest.dto.VisitFieldsDto;
import org.springframework.petmanagement.rest.dto.VisitPrescriptionDto;
import org.springframework.petmanagement.rest.dto.VisitPrescriptionFieldsDto;
import org.springframework.petmanagement.service.VisitPrescriptionService;
import org.springframework.petmanagement.service.VisitService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class VisitController implements VisitsApi {

    private final VisitService visitService;
    private final VisitMapper visitMapper;
    private final VisitPrescriptionService visitPrescriptionService;
    private final VisitPrescriptionMapper visitPrescriptionMapper;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final ClinicRepository clinicRepository;
    private final PrescriptionRepository prescriptionRepository;

    public VisitController(VisitService visitService,
                          VisitMapper visitMapper,
                          VisitPrescriptionService visitPrescriptionService,
                          VisitPrescriptionMapper visitPrescriptionMapper,
                          UserRepository userRepository,
                          PetRepository petRepository,
                          ClinicRepository clinicRepository,
                          PrescriptionRepository prescriptionRepository) {
        this.visitService = visitService;
        this.visitMapper = visitMapper;
        this.visitPrescriptionService = visitPrescriptionService;
        this.visitPrescriptionMapper = visitPrescriptionMapper;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.clinicRepository = clinicRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    @PreAuthorize("hasAnyRole(@roles.ADMIN, @roles.CLINIC_ADMIN)")
    @Override
    public ResponseEntity<List<VisitDto>> listVisits(UUID petId) {
        List<Visit> visits = visitService.listVisits(petId);
        return ResponseEntity.ok(visitMapper.toVisitDtoList(visits));
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
        Visit visit = visitMapper.toVisit(visitFieldsDto);
        
        // Set related entities
        if (visitFieldsDto.getUserId() == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = userRepository.findById(visitFieldsDto.getUserId());
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        visit.setUser(user);
        
        if (visitFieldsDto.getPetId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Pet pet = petRepository.findById(visitFieldsDto.getPetId());
        if (pet == null) {
            return ResponseEntity.badRequest().build();
        }
        visit.setPet(pet);
        
        if (visitFieldsDto.getClinicId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Clinic clinic = clinicRepository.findById(visitFieldsDto.getClinicId());
        if (clinic == null) {
            return ResponseEntity.badRequest().build();
        }
        visit.setClinic(clinic);
        
        Visit saved = visitService.createVisit(visitFieldsDto);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
            UriComponentsBuilder.newInstance()
                .path("/api/visits/{id}")
                .buildAndExpand(saved.getId())
                .toUri()
        );
        return new ResponseEntity<>(visitMapper.toVisitDto(saved), headers, HttpStatus.CREATED);
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
            VisitPrescription saved = visitPrescriptionService.createVisitPrescription(visitPrescriptionFieldsDto);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(
                UriComponentsBuilder.newInstance()
                    .path("/api/visits/{visitId}/prescriptions/{id}")
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
