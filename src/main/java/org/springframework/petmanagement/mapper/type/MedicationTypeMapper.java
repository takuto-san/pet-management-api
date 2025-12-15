package org.springframework.petmanagement.mapper;

import org.mapstruct.Mapper;
import org.springframework.petmanagement.model.type.MedicationType;
import org.springframework.petmanagement.rest.dto.PrescriptionCategoryDto;

@Mapper(componentModel = "spring")
public interface MedicationTypeMapper {

    MedicationType toMedicationType(PrescriptionCategoryDto prescriptionCategoryDto);

    PrescriptionCategoryDto toPrescriptionCategoryDto(MedicationType medicationType);
}
