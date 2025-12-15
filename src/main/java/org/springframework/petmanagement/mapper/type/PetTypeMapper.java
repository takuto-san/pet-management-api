package org.springframework.petmanagement.mapper.type;

import org.mapstruct.Mapper;
import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.rest.dto.PetTypeDto;

@Mapper(componentModel = "spring")
public interface PetTypeMapper {

    PetType toPetType(PetTypeDto petTypeDto);

    PetTypeDto toPetTypeDto(PetType petType);
}
