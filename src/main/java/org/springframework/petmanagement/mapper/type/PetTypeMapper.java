package org.springframework.petmanagement.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.rest.dto.PetTypeDto;

@Mapper(componentModel = "spring")
public interface PetTypeMapper {

    PetType toPetType(PetTypeDto petTypeDto);

    PetTypeDto toPetTypeDto(PetType petType);

    List<PetTypeDto> toPetTypeDtos(Collection<PetType> petTypes);
}
