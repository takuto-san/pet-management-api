package org.springframework.petmanagement.model.type.converter;

import org.springframework.petmanagement.model.type.ItemType;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ItemTypeConverter extends EnumConverter<ItemType> {

}