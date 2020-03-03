package com.kidscademy.atlas.studio.model;

import javax.persistence.Embeddable;

import js.lang.Displayable;
import js.util.Strings;

@Embeddable
public class Taxon implements Displayable {
    private String name;
    private String value;

    public Taxon() {

    }

    public Taxon(String name) {
	this.name = name;
    }

    public Taxon(String name, String value) {
	this.name = name;
	this.value = value;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    @Override
    public String toDisplay() {
	return Strings.concat(name, ' ', value);
    }

    @Override
    public String toString() {
	return Strings.toString(name, value);
    }
}
