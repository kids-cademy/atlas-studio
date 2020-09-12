package com.kidscademy.atlas.studio.dto;

import com.kidscademy.atlas.studio.model.TaxonUnit;

public class TaxonUnitTranslation
{
  private int id;
  private String name;
  private String display;
  private String translation;

  public TaxonUnitTranslation() {
  }

  public TaxonUnitTranslation(TaxonUnit taxonUnit, String translation) {
    this.id = taxonUnit.getId();
    this.name = taxonUnit.getName();
    this.display = taxonUnit.getDisplay();
    this.translation = translation;
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

  public String getTranslation() {
    return translation;
  }
}
