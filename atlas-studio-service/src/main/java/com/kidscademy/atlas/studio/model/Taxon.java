package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import js.util.Strings;

@Entity
public class Taxon
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
    return meta.getName() == null;
  }

  @Override
  public String toString() {
    return Strings.toString(meta.getName(), value);
  }
}
