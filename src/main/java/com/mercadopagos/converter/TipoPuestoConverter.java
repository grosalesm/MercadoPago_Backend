package com.mercadopagos.converter;

import com.mercadopagos.enums.TipoPuesto;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TipoPuestoConverter implements AttributeConverter<TipoPuesto, String> {

    @Override
    public String convertToDatabaseColumn(TipoPuesto attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public TipoPuesto convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return TipoPuesto.valueOf(dbData);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}