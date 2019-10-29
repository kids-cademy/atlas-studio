package com.kidscademy.atlas.studio.search;

import java.io.IOException;

import js.tools.commons.util.Params;

/**
 * Create direct index for an object of a specified type.
 * 
 * @author Iulian Rotaru
 */
public class ObjectIndexer<T, K extends Comparable<K>> {
    private final ObjectFields<T> fields;

    public ObjectIndexer(ObjectFields<T> fields) {
	this.fields = fields;
    }

    public DirectIndex<K> scanObject(T object, K key) throws IOException {
	Params.notNull(object, "Object");
	Params.notNull(key, "Object key");
	DirectIndex<K> directIndex = new DirectIndex<>(key);
	for (ObjectFields.Field field : fields.iterable(object)) {
	    directIndex.add(field.getName(), field.getText(), field.getRelevance());
	}
	return directIndex;
    }
}
