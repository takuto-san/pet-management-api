package org.springframework.petmanagement.model.type.converter;

import org.springframework.petmanagement.model.type.MedicationType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MedicationTypeConverter implements AttributeConverter<MedicationType, String> {
    @Override
    public String convertToDatabaseColumn(MedicationType attribute) {
        return (attribute == null) ? null : attribute.name().toLowerCase();
    }

    @Override
    public MedicationType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return MedicationType.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}