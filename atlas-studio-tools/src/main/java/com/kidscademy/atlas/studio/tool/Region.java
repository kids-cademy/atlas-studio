package com.kidscademy.atlas.studio.tool;

class Region {
    String name;
    Area area;

    public String getName() {
        return name;
    }

    public Area getArea() {
        return area;
    }

    enum Area {
	ALL, CENTRAL, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST
    }
}
