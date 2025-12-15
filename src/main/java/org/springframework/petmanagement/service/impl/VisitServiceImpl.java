package org.springframework.petmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.petmanagement.mapper.VisitMapper;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.repository.ClinicRepository;
import org.springframework.petmanagement.repository.PetRepository;
import org.springframework.petmanagement.repository.VisitRepository;
import org.springframework.petmanagement.rest.dto.VisitFieldsDto;
import org.springframework.petmanagement.service.VisitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final PetRepository petRepository;
    private final ClinicRepository clinicRepository;
    private final VisitMapper visitMapper;

    public VisitServiceImpl(VisitRepository visitRepository,
                            PetRepository petRepository,
                            ClinicRepository clinicRepository,
                            VisitMapper visitMapper) {
        this.visitRepository = visitRepository;
        this.petRepository = petRepository;
        this.clinicRepository = clinicRepository;
        this.visitMapper = visitMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Visit> listVisits(@Nullable UUID petId, Pageable pageable) {
        if (petId != null) {
            return visitRepository.findByPetId(petId, pageable);
        }
        return visitRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Visit> getVisit(UUID id) {
        return Optional.ofNullable(visitRepository.findById(id));
    }

    @Override
    public Visit createVisit(VisitFieldsDto fields) {
        Pet pet = petRepository.findById(fields.getPetId());
        if (pet == null) {
            throw new IllegalArgumentException("Pet not found: " + fields.getPetId());
        }
        Clinic clinic = clinicRepository.findById(fields.getClinicId());
        if (clinic == null) {
            throw new IllegalArgumentException("Clinic not found: " + fields.getClinicId());
        }
        Visit visit = visitMapper.toVisit(fields);
        visit.setPet(pet);
        visit.setClinic(clinic);
        visitRepository.save(visit);
        return visit;
    }

    @Override
    public Visit updateVisit(UUID id, VisitFieldsDto fields) {
        Visit visit = visitRepository.findById(id);
        if (visit == null) {
            throw new IllegalArgumentException("Visit not found: " + id);
        }
        visitMapper.updateVisitFromFields(fields, visit);
        visitRepository.save(visit);
        return visit;
    }

    @Override
    public void deleteVisit(UUID id) {
        Visit visit = visitRepository.findById(id);
        if (visit == null) throw new IllegalArgumentException("visit not found");
        visitRepository.delete(visit);
    }
}
