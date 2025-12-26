package org.springframework.petmanagement.service;

import java.util.List;
import java.util.UUID;

import org.springframework.petmanagement.model.Space;

public interface SpaceService {

    List<Space> findAllByUserId(UUID userId);

    Space save(Space space);

    Space findById(UUID id);

    void deleteById(UUID id);
}
