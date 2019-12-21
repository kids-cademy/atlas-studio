package com.kidscademy.atlas.studio.dao;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.kidscademy.atlas.studio.model.FeaturesClass;

@Converter(autoApply = true)
public class FeaturesClassConverter implements AttributeConverter<FeaturesClass, String> {

    @Override
    public String convertToDatabaseColumn(FeaturesClass state) {
	return state.name();
    }

    @Override
    public FeaturesClass convertToEntityAttribute(String value) {
	return FeaturesClass.valueOf(value);
    }
}
