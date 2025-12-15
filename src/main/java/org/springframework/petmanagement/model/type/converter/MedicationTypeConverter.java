package org.springframework.petmanagement.model.type.converter;

import org.springframework.petmanagement.model.type.MedicationType;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MedicationTypeConverter extends EnumConverter<MedicationType> {
}