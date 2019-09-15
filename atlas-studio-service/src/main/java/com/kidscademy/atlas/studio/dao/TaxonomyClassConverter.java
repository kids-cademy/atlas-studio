package com.kidscademy.atlas.studio.dao;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.kidscademy.atlas.studio.model.TaxonomyClass;

@Converter(autoApply = true)
public class TaxonomyClassConverter implements AttributeConverter<TaxonomyClass, String> {

    @Override
    public String convertToDatabaseColumn(TaxonomyClass state) {
	return state.name();
    }

    @Override
    public TaxonomyClass convertToEntityAttribute(String value) {
	return TaxonomyClass.valueOf(value);
    }
}
