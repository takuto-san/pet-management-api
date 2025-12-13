package org.springframework.petmanagement.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.petmanagement.model.type.PetType;
import org.springframework.petmanagement.rest.dto.PetTypeDto;
import org.springframework.petmanagement.rest.dto.PetTypeFieldsDto;

@Mapper(componentModel = "spring", uses = DateTimeMapper.class)
public interface PetTypeMapper {

    PetType toPetType(PetTypeDto petTypeDto);

    @Mapping(target = "id", ignore = true)
    PetType toPetType(PetTypeFieldsDto petTypeFieldsDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updatePetTypeFromFields(PetTypeFieldsDto petTypeFieldsDto, @MappingTarget PetType currentPetType);

    PetTypeDto toPetTypeDto(PetType petType);

    PetTypeFieldsDto toPetTypeFieldsDto(PetType petType);

    List<PetTypeDto> toPetTypeDtos(Collection<PetType> petTypes);
}