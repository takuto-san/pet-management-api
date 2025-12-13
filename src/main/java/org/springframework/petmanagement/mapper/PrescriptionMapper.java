package org.springframework.petmanagement.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.rest.dto.PrescriptionDto;
import org.springframework.petmanagement.rest.dto.PrescriptionFieldsDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = DateTimeMapper.class)
public interface PrescriptionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Prescription toPrescription(PrescriptionFieldsDto prescriptionFieldsDto);

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    PrescriptionDto toPrescriptionDto(Prescription prescription);

    List<PrescriptionDto> toPrescriptionDtoList(List<Prescription> prescriptions);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updatePrescriptionFromFields(PrescriptionFieldsDto prescriptionFieldsDto, @MappingTarget Prescription prescription);
}