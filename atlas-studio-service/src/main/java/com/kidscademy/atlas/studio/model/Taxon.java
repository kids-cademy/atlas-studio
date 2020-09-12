package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import js.lang.Displayable;
import js.util.Strings;

@Entity
public class Taxon implements Displayable
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  private TaxonMeta meta;

  private String value;

  public Taxon() {

  }

  public Taxon(TaxonMeta meta, String value) {
    this.meta = meta;
    this.value = value;
  }

  public int getId() {
    return id;
  }

  public TaxonMeta getMeta() {
    return meta;
  }

  public String getName() {
    return meta.getName();
  }

  public String getDisplay() {
    return meta.getDisplay();
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public boolean isWildcard() {
    return meta.getUnit() == null;
  }

  @Override
  public String toDisplay() {
    return Strings.concat(meta.getDisplay(), ' ', value);
  }

  @Override
  public String toString() {
    return Strings.toString(meta.getUnit(), value);
  }
}
