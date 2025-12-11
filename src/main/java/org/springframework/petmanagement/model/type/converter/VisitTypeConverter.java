package org.springframework.petmanagement.model.type.converter;

import org.springframework.petmanagement.model.type.VisitType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VisitTypeConverter implements AttributeConverter<VisitType, String> {
    @Override
    public String convertToDatabaseColumn(VisitType attribute) {
        return (attribute == null) ? null : attribute.name().toLowerCase();
    }

    @Override
    public VisitType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return VisitType.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}