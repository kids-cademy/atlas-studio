package com.kidscademy.atlas.studio.model;

public class ApiDescriptor implements Comparable<ApiDescriptor> {
    private final String name;
    private final String description;

    public ApiDescriptor(API api) {
	this.name = api.name();
	this.description = api.description();
    }

    public String getName() {
	return name;
    }

    public String getDescription() {
	return description;
    }

    @Override
    public int compareTo(ApiDescriptor that) {
	return this.name.compareTo(that.name);
    }
}
