package org.springframework.petmanagement.model.type.converter;

import org.springframework.petmanagement.model.type.RoleType;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleTypeConverter extends EnumConverter<RoleType> {
}