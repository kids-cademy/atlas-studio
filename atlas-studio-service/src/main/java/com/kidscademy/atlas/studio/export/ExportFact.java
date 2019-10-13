package com.kidscademy.atlas.studio.export;

import java.util.Map;

@SuppressWarnings("unused")
public class ExportFact {
    private final String name;
    private final String value;

    ExportFact(Map.Entry<String, String> fact) {
	this.name = fact.getKey();
	this.value = fact.getValue();
    }
}
