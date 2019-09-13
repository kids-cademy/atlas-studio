package com.kidscademy.atlas.studio.model;

import javax.persistence.Embeddable;

@Embeddable
public class Taxon {
    private String name;
    private String value;

    public Taxon() {

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
}
