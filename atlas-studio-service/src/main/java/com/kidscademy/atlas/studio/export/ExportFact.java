package com.kidscademy.atlas.studio.export;

import java.util.Map;

import com.kidscademy.atlas.studio.search.SearchWord;

import js.util.Strings;

public class ExportFact implements SearchWord {
    private final String name;
    private final String value;

    ExportFact(Map.Entry<String, String> fact) {
	this.name = fact.getKey();
	this.value = fact.getValue();
    }

    @Override
    public String toSearchWord() {
	return Strings.concat(name, ' ', value);
    }
}
