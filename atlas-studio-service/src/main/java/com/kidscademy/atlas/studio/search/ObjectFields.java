package com.kidscademy.atlas.studio.search;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import js.converter.Converter;
import js.converter.ConverterRegistry;
import js.lang.Displayable;
import js.util.Classes;
import js.util.Params;
import js.util.Types;

/**
 * When create a direct index for and object is not mandatory to use all fields
 * from that object. This class is a list of used object fields.
 * <p>
 * Also this class convey information about fields relevance. The order of
 * fields matters; Fields should be added in the fields relevance ascending
 * order. For example a 'description' field may have less relevance that
 * 'title'; therefore 'description' should be added before 'text'.
 * 
 * @author Iulian Rotaru
 */
public class ObjectFields<T> {
    private final Class<T> type;
    private final List<Meta> metas = new ArrayList<>();

    private int relevance;

    public ObjectFields(Class<T> type) {
	this.type = type;
	this.relevance = 1;
    }

    public void addField(String fieldName) throws NoSuchMethodException {
	Params.notNullOrEmpty(fieldName, "Field name");
	Params.LT(metas.size(), 30, "Fields number");
	metas.add(new Meta(fieldName, relevance));
	relevance <<= 1;
    }

    public Iterable<Field> iterable(T object) {
	List<Field> fields = new ArrayList<>();
	for (Meta meta : metas) {
	    fields.add(new Field(object, meta.getGetter(), meta.getRelevance()));
	}
	return fields;
    }
    
    private class Meta {
	private final Method getter;
	private final int relevance;

	public Meta(String fieldName, int relevance) throws NoSuchMethodException {
	    this.getter = Classes.getGetter(type, fieldName);
	    this.relevance = relevance;
	}

	public Method getGetter() {
	    return getter;
	}

	public int getRelevance() {
	    return relevance;
	}
    }

    public static class Field {
	private final Converter converter;
	private final Object object;
	private final Method getter;
	private final int relevance;

	public Field(Object object, Method getter, int relevance) {
	    this.converter = ConverterRegistry.getConverter();
	    this.object = object;
	    this.getter = getter;
	    this.relevance = relevance;
	}

	public String getName() {
	    return getter.getName();
	}

	public String getText()  {
	    Object value;
	    try {
		value = Classes.invoke(object, getter);
	    } catch (Exception e) {
		return null;
	    }
	    if (value instanceof String) {
		return (String) value;
	    }

	    StringBuilder text = new StringBuilder();
	    boolean first = true;
	    for (Object item : Types.asIterable(value)) {
		if (first) {
		    first = false;
		} else {
		    text.append(' ');
		}
		
		if (item instanceof Displayable) {
		    text.append(((Displayable) item).toDisplay());
		} else {
		    text.append(converter.asString(item));
		}
	    }
	    return text.toString();
	}

	public int getRelevance() {
	    return relevance;
	}
    }
}
