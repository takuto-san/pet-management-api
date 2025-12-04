package org.springframework.petmanagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.petmanagement.model.Owner;
import org.springframework.petmanagement.rest.dto.OwnerDto;
import org.springframework.petmanagement.rest.dto.OwnerFieldsDto;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", uses = PetMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OwnerMapper {

    @Mapping(source = "pets", target = "pets")
    OwnerDto toOwnerDto(Owner owner);

    @Mapping(source = "pets", target = "pets")
    Owner toOwner(OwnerDto ownerDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pets", ignore = true)
    Owner toOwner(OwnerFieldsDto ownerDto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pets", ignore = true)
    Owner toOwner(OwnerFieldsDto ownerFieldsDto, @MappingTarget Owner currentOwner);

    List<OwnerDto> toOwnerDtoCollection(Collection<Owner> ownerCollection);

    Collection<Owner> toOwners(Collection<OwnerDto> ownerDtos);
}