package org.springframework.petmanagement.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.rest.dto.VisitDto;
import org.springframework.petmanagement.rest.dto.VisitFieldsDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "clinic", ignore = true)
    Visit toVisit(VisitFieldsDto visitFieldsDto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "clinic.id", target = "clinicId")
    VisitDto toVisitDto(Visit visit);

    List<VisitDto> toVisitDtoList(List<Visit> visits);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "clinic", ignore = true)
    void updateVisitFromFields(VisitFieldsDto visitFieldsDto, @MappingTarget Visit visit);
}
