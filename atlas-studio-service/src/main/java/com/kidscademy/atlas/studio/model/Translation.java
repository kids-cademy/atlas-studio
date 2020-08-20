package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.kidscademy.atlas.studio.model.TranslationKey.Discriminator;

@Entity
@IdClass(TranslationKey.class)
public class Translation
{
  @Id
  @Enumerated(EnumType.STRING)
  private Discriminator discriminator;
  @Id
  private int objectId;
  @Id
  private String language;
  
  private String text;

  public void setDiscriminator(Discriminator discriminator) {
    this.discriminator = discriminator;
  }

  public void setObjectId(int objectId) {
    this.objectId = objectId;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getLanguage() {
    return language;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }
  
  public enum State {
    /** Translation record is created on database with content loaded from translation service. */
    CREATED,
    /** */
    DIRTY,
    /**  */
    ERRATA
  }
}
