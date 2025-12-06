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

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {

    @Mapping(target = "id", ignore = true)
    Prescription toPrescription(PrescriptionFieldsDto prescriptionFieldsDto);

    PrescriptionDto toPrescriptionDto(Prescription prescription);

    List<PrescriptionDto> toPrescriptionDtoList(List<Prescription> prescriptions);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updatePrescriptionFromFields(PrescriptionFieldsDto prescriptionFieldsDto, @MappingTarget Prescription prescription);
}
