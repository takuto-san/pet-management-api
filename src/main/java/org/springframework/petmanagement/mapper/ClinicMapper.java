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

@Mapper(componentModel = "spring")
public interface ClinicMapper {

    @Mapping(target = "id", ignore = true)
    Clinic toClinic(ClinicFieldsDto clinicFieldsDto);

    ClinicDto toClinicDto(Clinic clinic);

    List<ClinicDto> toClinicDtoList(List<Clinic> clinics);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateClinicFromFields(ClinicFieldsDto clinicFieldsDto, @MappingTarget Clinic clinic);
}
