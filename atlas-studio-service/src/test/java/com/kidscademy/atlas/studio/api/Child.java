package com.kidscademy.atlas.studio.api;

import javax.persistence.Embeddable;

@Embeddable
class Child {
    private String name;

    public Child() {
    }

    public Child(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }
}