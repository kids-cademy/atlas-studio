package com.kidscademy.atlas.studio.model;

import javax.persistence.Embeddable;

import js.lang.Displayable;

@Embeddable
public class Region implements Displayable {
    private String name;
    private Area area;

    public Region() {
	area = Area.ALL;
    }

    public Region(String name) {
	this();
	this.name = name;
    }

    public Region(String name, Area area) {
	this(name);
	this.area = area;
    }

    public String getName() {
	return name;
    }

    public Area getArea() {
	return area;
    }

    @Override
    public String toDisplay() {
	return name;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((area == null) ? 0 : area.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Region other = (Region) obj;
	if (area != other.area)
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

    public enum Area {
	ALL, CENTRAL, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST
    }
}
