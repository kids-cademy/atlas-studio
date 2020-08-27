package com.kidscademy.atlas.studio.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.kidscademy.atlas.studio.model.TranslationKey.Discriminator;

@Entity
@Table(name = "translation")
@IdClass(TranslationKey.class)
public class TranslationData
{
  @Id
  @Enumerated(EnumType.STRING)
  private Discriminator discriminator;
  @Id
  private int objectId;
  @Id
  private String language;

  private Date timestamp;
  private String text;

  public TranslationData() {
  }

  public TranslationData(Discriminator discriminator, int objectId, String language) {
    this.discriminator = discriminator;
    this.objectId = objectId;
    this.language = language;
    this.timestamp = new Date(2160000);
  }

  public TranslationData(TranslationKey key) {
    this.discriminator = key.getDiscriminator();
    this.objectId = key.getObjectId();
    this.language = key.getLanguage();
  }

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

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public enum State
  {
    /** Translation record is created on database with content loaded from translation service. */
    CREATED,
    /** */
    DIRTY,
    /**  */
    ERRATA
  }
}
