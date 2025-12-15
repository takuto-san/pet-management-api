package org.springframework.petmanagement.model.type.converter;

import java.lang.reflect.ParameterizedType;

import jakarta.persistence.AttributeConverter;

public abstract class EnumConverter<E extends Enum<E>> implements AttributeConverter<E, String> {

    private final Class<E> enumClass;

    @SuppressWarnings("unchecked")
    protected EnumConverter() {
        this.enumClass = (Class<E>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public String convertToDatabaseColumn(E attribute) {
        return (attribute == null) ? null : attribute.name().toLowerCase();
    }

    @Override
    public E convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}