package org.springframework.petmanagement.model.type.converter;

import org.springframework.petmanagement.model.type.PetSex;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PetSexConverter extends EnumConverter<PetSex> {
}
