package com.kidscademy.atlas.studio.model;

import java.io.Serializable;

public class TranslationKey implements Serializable
{
  private static final long serialVersionUID = -8244180420001379481L;

  private Discriminator discriminator;
  private int objectId;
  private String language;

  public TranslationKey() {
  }

  public TranslationKey(Discriminator discriminator, int objectId, String language) {
    this.discriminator = discriminator;
    this.objectId = objectId;
    this.language = language;
  }

  public Discriminator getDiscriminator() {
    return discriminator;
  }

  public int getObjectId() {
    return objectId;
  }

  public String getLanguage() {
    return language;
  }

  public enum Discriminator
  {
    OBJECT_DISPLAY, OBJECT_ALIASES, OBJECT_DEFINITION, OBJECT_DESCRIPTION, OBJECT_SAMPLE_TITLE, FACT_KEY, FACT_VALUE, TAXON_META_DISPLAY, TAXON_VALUE, FEATURE_META_DISPLAY, EXTERNAL_SOURCE_DISPLAY, EXTERNAL_SOURCE_DEFINITION_TEMPLATE;
  }
}
