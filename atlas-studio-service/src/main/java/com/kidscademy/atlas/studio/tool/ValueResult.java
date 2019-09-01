package com.kidscademy.atlas.studio.tool;

import js.converter.Converter;
import js.converter.ConverterRegistry;

public class ValueResult<T> implements ResultParser {
    private final Converter converter;
    private final StringBuilder builder;

    public ValueResult() {
	this.converter = ConverterRegistry.getConverter();
	this.builder = new StringBuilder();
    }

    @Override
    public void parse(String line) {
	builder.append(line);
    }

    public T getValue(Class<T> type) {
	return converter.asObject(builder.toString(), type);
    }
}
