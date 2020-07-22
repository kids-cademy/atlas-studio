package com.kidscademy.atlas.studio.model;

import javax.persistence.Embeddable;

import js.tiny.container.annotation.TestConstructor;

@Embeddable
public class DescriptionMeta {
    private String name;
    private String definition;

    public DescriptionMeta() {
    }

    @TestConstructor
    public DescriptionMeta(String name, String definition) {
	this.name = name;
	this.definition = definition;
    }

    public String getName() {
	return name;
    }

    public String getDefinition() {
	return definition;
    }

    @Override
    public String toString() {
	return "DescriptionMeta [name=" + name + "]";
    }
}
