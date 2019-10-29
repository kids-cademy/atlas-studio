package com.kidscademy.atlas.studio.export;

import java.util.Map;

import js.lang.Displayable;
import js.util.Strings;

public class ExportFact implements Displayable {
    private final String name;
    private final String value;

    ExportFact(Map.Entry<String, String> fact) {
	this.name = fact.getKey();
	this.value = fact.getValue();
    }

    @Override
    public String toDisplay() {
	return Strings.concat(name, ' ', value);
    }
}
