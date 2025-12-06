package org.springframework.petmanagement.service.impl;

import org.springframework.lang.Nullable;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.repository.VisitRepository;
import org.springframework.petmanagement.service.VisitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;

    public VisitServiceImpl(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Visit> findAll(@Nullable UUID petId) {
        if (petId != null) {
            Collection<Visit> visits = visitRepository.findByPetId(petId);
            return new ArrayList<>(visits);
        }
        return new ArrayList<>(visitRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Visit> findById(UUID id) {
        return Optional.ofNullable(visitRepository.findById(id));
    }

    @Override
    public Visit save(Visit visit) {
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
