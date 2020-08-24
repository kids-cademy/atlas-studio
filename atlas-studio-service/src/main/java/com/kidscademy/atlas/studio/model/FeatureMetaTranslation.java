package com.kidscademy.atlas.studio.model;

public class FeatureMetaTranslation
{
  private int id;
  private String name;
  private String display;
  private String translation;

  public FeatureMetaTranslation() {
  }

  public FeatureMetaTranslation(FeatureMeta meta, String translation) {
    this.id = meta.getId();
    this.name = meta.getName();
    this.display = meta.getDisplay();
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
