package com.kidscademy.atlas.studio.model;

import javax.persistence.Embeddable;

@Embeddable
public class DescriptionMeta {
    private String name;
    private String definition;

    public DescriptionMeta() {
    }

    public String getName() {
	return name;
    }

    public DescriptionMeta(String name) {
	this.name = name;
    }

    public String getDefinition() {
	return definition;
    }
}
