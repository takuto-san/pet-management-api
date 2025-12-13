package org.springframework.petmanagement.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.petmanagement.model.VisitPrescription;
import org.springframework.petmanagement.rest.dto.VisitPrescriptionDto;
import org.springframework.petmanagement.rest.dto.VisitPrescriptionFieldsDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = DateTimeMapper.class)
public interface VisitPrescriptionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visit", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VisitPrescription toVisitPrescription(VisitPrescriptionFieldsDto visitPrescriptionFieldsDto);

    @Mapping(source = "visit.id", target = "visitId")
    @Mapping(source = "prescription.id", target = "prescriptionId")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    VisitPrescriptionDto toVisitPrescriptionDto(VisitPrescription visitPrescription);

    List<VisitPrescriptionDto> toVisitPrescriptionDtoList(List<VisitPrescription> visitPrescriptions);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visit", ignore = true)
    @Mapping(target = "prescription", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateVisitPrescriptionFromFields(VisitPrescriptionFieldsDto visitPrescriptionFieldsDto, @MappingTarget VisitPrescription visitPrescription);
}