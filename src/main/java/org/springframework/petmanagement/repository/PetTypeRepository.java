package org.springframework.petmanagement.repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.petmanagement.model.type.PetType;

public interface PetTypeRepository {

    default void save(PetType petType) throws DataAccessException {
        throw new UnsupportedOperationException("PetType is an enum and cannot be saved");
    }

    default void delete(PetType petType) throws DataAccessException {
        throw new UnsupportedOperationException("PetType is an enum and cannot be deleted");
    }

    default PetType findById(UUID id) throws DataAccessException {
        return null;
    }

    default PetType findByName(String name) throws DataAccessException {
        return Arrays.stream(PetType.values())
                .filter(pt -> pt.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    default Collection<PetType> findAll() throws DataAccessException {
        return Arrays.asList(PetType.values());
    }

}
