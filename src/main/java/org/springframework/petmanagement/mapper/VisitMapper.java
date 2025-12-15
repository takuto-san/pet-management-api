package org.springframework.petmanagement.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.petmanagement.model.Visit;
import org.springframework.petmanagement.rest.dto.VisitDto;
import org.springframework.petmanagement.rest.dto.VisitFieldsDto;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class, VisitTypeMapper.class})
public interface VisitMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "clinic", ignore = true)
    @Mapping(target = "visitType", source = "visitType")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Visit toVisit(VisitFieldsDto visitFieldsDto);

    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "clinic.id", target = "clinicId")
    @Mapping(target = "visitType", source = "visitType")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    VisitDto toVisitDto(Visit visit);

    List<VisitDto> toVisitDtoList(List<Visit> visits);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "clinic", ignore = true)
    @Mapping(target = "visitType", source = "visitType")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateVisitFromFields(VisitFieldsDto visitFieldsDto, @MappingTarget Visit visit);
}
