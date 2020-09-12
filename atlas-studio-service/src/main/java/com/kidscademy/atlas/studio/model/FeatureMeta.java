package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import js.tiny.container.annotation.TestConstructor;

@Entity
public class FeatureMeta
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Enumerated(EnumType.STRING)
  private PhysicalQuantity quantity;

  private String name;
  private String display;
  private String definition;

  public FeatureMeta() {
  }

  @TestConstructor
  public FeatureMeta(int id, PhysicalQuantity quantity, String name, String display, String definition) {
    super();
    this.id = id;
    this.quantity = quantity;
    this.name = name;
    this.display = display;
    this.definition = definition;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public PhysicalQuantity getQuantity() {
    return quantity;
  }

  public String getDisplay() {
    return display;
  }

  public String getDefinition() {
    return definition;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj) return true;
    if(obj == null) return false;
    if(getClass() != obj.getClass()) return false;
    FeatureMeta other = (FeatureMeta)obj;
    if(id != other.id) return false;
    return true;
  }

  public static FeatureMeta create() {
    FeatureMeta featureMeta = new FeatureMeta();
    return featureMeta;
  }
}
