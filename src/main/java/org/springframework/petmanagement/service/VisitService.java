package org.springframework.petmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.lang.Nullable;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.rest.dto.VisitFieldsDto;

public interface VisitService {
    List<Visit> listVisits(@Nullable UUID petId);
    Optional<Visit> getVisit(UUID id);
    Visit createVisit(VisitFieldsDto fields);
    Visit updateVisit(UUID id, VisitFieldsDto fields);
    void deleteVisit(UUID id);
}
