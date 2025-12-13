package org.springframework.petmanagement.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.petmanagement.model.Clinic;
import org.springframework.petmanagement.rest.dto.ClinicDto;
import org.springframework.petmanagement.rest.dto.ClinicFieldsDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = DateTimeMapper.class)
public interface ClinicMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Clinic toClinic(ClinicFieldsDto clinicFieldsDto);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    ClinicDto toClinicDto(Clinic clinic);

    List<ClinicDto> toClinicDtoList(List<Clinic> clinics);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateClinicFromFields(ClinicFieldsDto clinicFieldsDto, @MappingTarget Clinic clinic);
}