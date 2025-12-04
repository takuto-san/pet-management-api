package org.springframework.petmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.petmanagement.model.Vet;
import org.springframework.petmanagement.rest.dto.VetDto;
import org.springframework.petmanagement.rest.dto.VetFieldsDto;

import java.util.Collection;

/**
 * Map Vet & VetoDto using mapstruct
 */
@Mapper(uses = SpecialtyMapper.class)
public interface VetMapper {
    Vet toVet(VetDto vetDto);

    @Mapping(target = "id", ignore = true)
    Vet toVet(VetFieldsDto vetFieldsDto);

    VetDto toVetDto(Vet vet);

    Collection<VetDto> toVetDtos(Collection<Vet> vets);
}
