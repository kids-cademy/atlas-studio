package com.kidscademy.atlas.studio.model;

public enum Gravity {
    CENTER("center"), NORTH("north"), NORTH_EAST("north-east"), EAST("east"), SOUTH_EAST("south-east"), SOUTH(
	    "south"), SOUTH_WEST("south-west"), WEST("west"), NORTH_WEST("north-west");

    private String value;

    private Gravity(String value) {
	this.value = value;
    }

    public String value() {
	return value;
    }
}
