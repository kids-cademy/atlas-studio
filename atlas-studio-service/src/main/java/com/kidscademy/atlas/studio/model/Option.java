package com.kidscademy.atlas.studio.model;

public class Option {
    private final String text;
    private final String value;

    public Option(String text, String value) {
	this.text = text;
	this.value = value;
    }

    public String getText() {
	return text;
    }

    public String getValue() {
	return value;
    }
}
