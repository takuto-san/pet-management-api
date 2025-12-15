package org.springframework.petmanagement.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.petmanagement.mapper.type.MedicationTypeMapper;
import org.springframework.petmanagement.model.Prescription;
import org.springframework.petmanagement.rest.dto.PrescriptionDto;
import org.springframework.petmanagement.rest.dto.PrescriptionFieldsDto;

@Mapper(componentModel = "spring", uses = {DateTimeMapper.class, MedicationTypeMapper.class})
public interface PrescriptionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Prescription toPrescription(PrescriptionFieldsDto prescriptionFieldsDto); // 入力フォーマット：PrescriptionFieldsDto → 出力フォーマット：Prescription（APIリクエスト → DB（保存用）エンティティ）

    @Mapping(target = "category", source = "category")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    PrescriptionDto toPrescriptionDto(Prescription prescription);             // 入力フォーマット：Prescription → 出力フォーマット：PrescriptionDto（DBエンティティ → APIレスポンス）

    List<PrescriptionDto> toPrescriptionDtoList(List<Prescription> prescriptions);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", source = "category")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updatePrescriptionFromFields(PrescriptionFieldsDto prescriptionFieldsDto, @MappingTarget Prescription prescription);
}
