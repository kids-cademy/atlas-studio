package com.kidscademy.atlas.studio.dao;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import js.util.Strings;

@Converter
public class StringsListConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> strings) {
	return Strings.join(strings, ',');
    }

    @Override
    public List<String> convertToEntityAttribute(String commaSeparatedStrings) {
	return Strings.split(commaSeparatedStrings, ',');
    }
}
