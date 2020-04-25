package com.kidscademy.atlas.studio.model;

import javax.persistence.Embeddable;

@Embeddable
public class DescriptionMeta {
    private String name;
    private String definition;

    public String getName() {
	return name;
    }

    public String getDefinition() {
	return definition;
    }
}
