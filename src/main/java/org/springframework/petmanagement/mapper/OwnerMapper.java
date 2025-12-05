package org.springframework.petmanagement.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.petmanagement.model.Owner;
import org.springframework.petmanagement.rest.dto.OwnerDto;
import org.springframework.petmanagement.rest.dto.OwnerFieldsDto;

import java.util.Collection;
import java.util.List;

@Mapper(
    componentModel = "spring",
    uses = PetMapper.class
)
public interface OwnerMapper {

    @Mapping(source = "pets", target = "pets")
    OwnerDto toOwnerDto(Owner owner);

    List<OwnerDto> toOwnerDtoCollection(Collection<Owner> ownerCollection);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pets", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Owner toOwner(OwnerFieldsDto ownerDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pets", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Owner toOwner(OwnerFieldsDto ownerFieldsDto, @MappingTarget Owner currentOwner);

    Collection<Owner> toOwners(Collection<OwnerDto> ownerDtos);
}