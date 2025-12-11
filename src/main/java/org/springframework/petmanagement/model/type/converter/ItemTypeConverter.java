package org.springframework.petmanagement.model.type.converter;

import org.springframework.petmanagement.model.type.ItemType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ItemTypeConverter implements AttributeConverter<ItemType, String> {
    @Override
    public String convertToDatabaseColumn(ItemType attribute) {
        return (attribute == null) ? null : attribute.name().toLowerCase();
    }

    @Override
    public ItemType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) return null;
        try {
            return ItemType.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}