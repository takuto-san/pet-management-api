package org.springframework.petmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.petmanagement.model.Pet;
import org.springframework.petmanagement.rest.dto.PetDto;
import org.springframework.petmanagement.rest.dto.PetFieldsDto;

import java.util.Collection;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = PetTypeMapper.class)
public interface PetMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "type", target = "type")
    PetDto toPetDto(Pet pet);

    Collection<PetDto> toPetsDto(Collection<Pet> pets);

    Collection<Pet> toPets(Collection<PetDto> pets);

    @Mapping(source = "ownerId", target = "owner.id")
    @Mapping(source = "type", target = "type")
    Pet toPet(PetDto petDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)

    @Mapping(source = "typeId", target = "type.id")
    Pet toPet(PetFieldsDto petFieldsDto);
    
    default UUID map(Integer id) {
        if (id == null) {
            return null;
        }
        return UUID.nameUUIDFromBytes(String.valueOf(id).getBytes());
    }

    default Integer map(UUID id) {
        if (id == null) {
            return null;
        }
        return null;
    }
}