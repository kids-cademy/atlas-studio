package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import js.tiny.container.annotation.TestConstructor;
import js.util.Strings;

/**
 * Taxonomic unity identified by a name and used to classify objects by a particular criterion, e.g kingdom, type, family. Beside its internal name it has a
 * display name used on user interface.
 * 
 * @author Iulian Rotaru
 */
@Entity
public class TaxonUnit
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  /** Unique, not null name. By convention, name is always a single English noun with all letters lowercase. */
  private String name;

  /** Display name used on user interface, subject of translation. */
  private String display;
  /** Taxonomic unit short description. */
  private String definition;

  @Transient
  private final String title = "Taxon Unit";

  public TaxonUnit() {
  }

  @TestConstructor
  public TaxonUnit(int id) {
    this.id = id;
  }

  @TestConstructor
  public TaxonUnit(String name) {
    this.name = name;
    this.display = Strings.toTitleCase(name);
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
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
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj) return true;
    if(obj == null) return false;
    if(getClass() != obj.getClass()) return false;
    TaxonUnit other = (TaxonUnit)obj;
    if(name == null) {
      if(other.name != null) return false;
    }
    else if(!name.equals(other.name)) return false;
    return true;
  }

  public static TaxonUnit create() {
    TaxonUnit taxonUnit = new TaxonUnit();
    return taxonUnit;
  }
}
