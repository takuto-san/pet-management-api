package org.springframework.petmanagement.model.type.converter;

import org.springframework.petmanagement.model.type.VisitType;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VisitTypeConverter extends EnumConverter<VisitType> {
    
}