package org.springframework.petmanagement.service;

import org.springframework.lang.Nullable;
import org.springframework.petmanagement.model.Visit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitService {
    List<Visit> findAll(@Nullable UUID petId);
    Optional<Visit> findById(UUID id);
    Visit save(Visit visit);
    void deleteVisit(UUID id);
}
