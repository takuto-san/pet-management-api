package org.springframework.petmanagement.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.Space;
import org.springframework.petmanagement.repository.SpaceRepository;
import org.springframework.petmanagement.service.SpaceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpaceServiceImpl implements SpaceService {

    private final SpaceRepository spaceRepository;

    public SpaceServiceImpl(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Space> findAllByUserId(UUID userId) {
        return spaceRepository.findByUserId(userId).stream().toList();
    }

    @Override
    public Space save(Space space) {
        return spaceRepository.save(space);
    }

    @Override
    @Transactional(readOnly = true)
    public Space findById(UUID id) {
        return spaceRepository.findById(id).orElseThrow(() -> new DataAccessException("Space not found") {
        });
    }

    @Override
    public void deleteById(UUID id) {
        spaceRepository.delete(findById(id));
    }
}
