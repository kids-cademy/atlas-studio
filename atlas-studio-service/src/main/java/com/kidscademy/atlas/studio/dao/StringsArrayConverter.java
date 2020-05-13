package com.kidscademy.atlas.studio.dao;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import js.util.Strings;

@Converter(autoApply = true)
public class StringsArrayConverter implements AttributeConverter<String[], String> {
    @Override
    public String convertToDatabaseColumn(String[] strings) {
	return Strings.join(strings, ',');
    }

    @Override
    public String[] convertToEntityAttribute(String commaSeparatedStrings) {
	List<String> strings = Strings.split(commaSeparatedStrings, ',');
	return strings.toArray(new String[strings.size()]);
    }
}
