package org.springframework.petmanagement.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.lang.Nullable;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.repository.VisitRepository;
import org.springframework.petmanagement.rest.dto.VisitFieldsDto;
import org.springframework.petmanagement.service.VisitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;

    public VisitServiceImpl(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Visit> listVisits(@Nullable UUID petId) {
        if (petId != null) {
            Collection<Visit> visits = visitRepository.findByPetId(petId);
            return new ArrayList<>(visits);
        }
        return new ArrayList<>(visitRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Visit> getVisit(UUID id) {
        return Optional.ofNullable(visitRepository.findById(id));
    }

    @Override
    public Visit createVisit(VisitFieldsDto fields) {
        // TODO: implement
        return null;
    }

    @Override
    public Visit updateVisit(UUID id, VisitFieldsDto fields) {
        // TODO: implement
        return null;
    }

    @Override
    public void deleteVisit(UUID id) {
        Visit visit = visitRepository.findById(id);
        if (visit == null) throw new IllegalArgumentException("visit not found");
        visitRepository.delete(visit);
    }
}
