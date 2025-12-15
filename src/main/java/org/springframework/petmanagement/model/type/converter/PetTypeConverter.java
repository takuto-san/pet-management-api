package org.springframework.petmanagement.model.type.converter;

import org.springframework.petmanagement.model.type.PetType;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PetTypeConverter extends EnumConverter<PetType> {
}
