package com.kidscademy.atlas.studio.model;

import java.util.Map;

import js.converter.Converter;
import js.converter.ConverterRegistry;

public class SearchFilter {
    private final Map<String, String> criteria;
    private final Converter converter;

    public SearchFilter(Map<String, String> criteria) {
	this.criteria = criteria;
	this.converter = ConverterRegistry.getConverter();
    }

    public Criterion criterion(String key) {
	return new CriterionImpl(criteria.get(key));
    }

    public <T> T get(String key, Class<T> type) {
	return converter.asObject(criteria.get(key), type);
    }

    public interface Criterion {
	boolean is(Object value);
    }

    private final class CriterionImpl implements Criterion {
	private final String value;

	public CriterionImpl(String value) {
	    this.value = value;
	}

	@Override
	public boolean is(Object exptected) {
	    return converter.asString(exptected).equals(value);
	}
    }
}
