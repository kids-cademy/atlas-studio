package com.kidscademy.atlas.studio.model;

import javax.persistence.Embeddable;

import js.lang.Displayable;

@Embeddable
public class Region implements Displayable
{
  private int id;
  private String name;
  private Area area;
  private String less;
  private Area lessArea;

  public Region() {
    area = Area.ALL;
    lessArea = Area.ALL;
  }

  public Region(String name) {
    this();
    this.name = name;
  }

  public Region(String name, Area area) {
    this(name);
    this.area = area;
  }

  public Region(String name, Area area, String less) {
    this(name);
    this.area = area;
    this.less = less;
  }

  public Region(String name, Area area, String less, Area lessArea) {
    this.name = name;
    this.area = area;
    this.less = less;
    this.lessArea = lessArea;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Area getArea() {
    return area;
  }

  public String getLess() {
    return less;
  }

  public Area getLessArea() {
    return lessArea;
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
    result = prime * result + ((less == null) ? 0 : less.hashCode());
    result = prime * result + ((lessArea == null) ? 0 : lessArea.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj) return true;
    if(obj == null) return false;
    if(getClass() != obj.getClass()) return false;
    Region other = (Region)obj;
    if(area != other.area) return false;
    if(less == null) {
      if(other.less != null) return false;
    }
    else if(!less.equals(other.less)) return false;
    if(lessArea != other.lessArea) return false;
    if(name == null) {
      if(other.name != null) return false;
    }
    else if(!name.equals(other.name)) return false;
    return true;
  }

  public enum Area
  {
    ALL, CENTRAL, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST
  }
}
