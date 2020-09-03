package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import js.tiny.container.annotation.TestConstructor;
import js.util.Strings;

/**
 * Taxonomic unity identified by a name and used to classify objects by a particular criterion, e.g kingdom, type, family. Beside its internal name it has a
 * display name used on user interface.
 * 
 * @author Iulian Rotaru
 *
 */
@Entity
public class TaxonUnit
{
  /** Unique, not null name used as primary key. By convention, name is always a single English noun with all letters lowercase. */
  @Id
  private String name;

  /** Display name used on user interface, subject of translation. */
  private String display;
  /** Taxonomic unit description. */
  private String description;

  public TaxonUnit() {
  }

  @TestConstructor
  public TaxonUnit(String name) {
    this.name = name;
    this.display = Strings.toTitleCase(name);
  }

  public String getName() {
    return name;
  }

  public String getDisplay() {
    return display;
  }

  public String getDescription() {
    return description;
  }
}