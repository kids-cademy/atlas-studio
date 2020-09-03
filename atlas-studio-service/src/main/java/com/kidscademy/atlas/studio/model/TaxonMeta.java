package com.kidscademy.atlas.studio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import js.tiny.container.annotation.TestConstructor;

@Entity
public class TaxonMeta
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private TaxonUnit unit;

  /**
   * Comma separated values allowed for this taxon or null if input is not constrained. User interface should use appropriate widgets: a select for list of
   * values or free text input if this allowed values list is null.
   */
  @Column(name = "\"VALUES\"")
  private String values;

  public TaxonMeta() {
  }

  @TestConstructor
  public TaxonMeta(TaxonUnit unit) {
    this.unit = unit;
  }

  @TestConstructor
  public TaxonMeta(TaxonUnit unit, String values) {
    this.unit = unit;
    this.values = values;
  }

  public int getId() {
    return id;
  }

  public String getUnit() {
    return unit.getName();
  }

  public String getDisplay() {
    return unit.getDisplay();
  }

  public String getValues() {
    return values;
  }
}
