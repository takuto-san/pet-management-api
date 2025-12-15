package org.springframework.petmanagement.model.type.converter;

import org.springframework.petmanagement.model.type.Currency;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CurrencyConverter extends EnumConverter<Currency> {
}
