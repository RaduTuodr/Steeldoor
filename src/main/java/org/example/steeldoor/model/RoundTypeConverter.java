package org.example.steeldoor.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.steeldoor.model.enums.RoundType;

@Converter(autoApply = true)
public class RoundTypeConverter implements AttributeConverter<RoundType, String> {

    @Override
    public String convertToDatabaseColumn(RoundType attribute) {
        return attribute.getDbValue();
    }

    @Override
    public RoundType convertToEntityAttribute(String dbData) {
        return RoundType.fromDbValue(dbData);
    }
}
