package org.springframework.petmanagement.mapper.type;

import org.mapstruct.Mapper;
import org.springframework.petmanagement.model.type.PetSex;
import org.springframework.petmanagement.rest.dto.PetSexDto;

@Mapper(componentModel = "spring")
public interface PetSexMapper {

    PetSex toPetSex(PetSexDto petSexDto);

    PetSexDto toPetSexDto(PetSex petSex);
}
