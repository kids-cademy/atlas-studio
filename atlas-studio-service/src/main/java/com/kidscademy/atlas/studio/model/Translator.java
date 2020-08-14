package com.kidscademy.atlas.studio.model;

import java.util.Collections;
import java.util.List;

import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.TranslationKey.Discriminator;

public class Translator
{
  public static final String DEFAULT_LANGUAGE = "EN";

  public static Translator getDefaultInstance() {
    return new Translator(DEFAULT_LANGUAGE);
  }

  public static boolean isDefaultLanguage(String language) {
    return DEFAULT_LANGUAGE.equals(language);
  }

  private final AtlasDao dao;
  private final String language;

  private Translator(String language) {
    this.dao = null;
    this.language = language;
  }

  public Translator(AtlasDao dao, String language) {
    this.dao = dao;
    this.language = language;
  }

  public boolean isDefaultLanguage() {
    return DEFAULT_LANGUAGE.equals(language);
  }

  public String getLanguage() {
    return language;
  }

  public String getAtlasObjectDisplay(int objectId) {
    return dao.getTranslation(new TranslationKey(Discriminator.OBJECT_DISPLAY, objectId, language));
  }

  public List<String> getAtlasObjectAliases(int objectId) {
    // TODO Auto-generated method stub
    return Collections.emptyList();
  }

  public String getAtlasObjectDefinition(int objectId) {
    return dao.getTranslation(new TranslationKey(Discriminator.OBJECT_DEFINITION, objectId, language));
  }

  public String getAtlasObjectDescription(int objectId) {
    return dao.getTranslation(new TranslationKey(Discriminator.OBJECT_DESCRIPTION, objectId, language));
  }

  public String getTaxonMetaDisplay(int taxonMetaId) {
    return dao.getTranslation(new TranslationKey(Discriminator.TAXON_META_DISPLAY, taxonMetaId, language));
  }

  public String getTaxonValue(int taxonId) {
    return dao.getTranslation(new TranslationKey(Discriminator.TAXON_VALUE, taxonId, language));
  }
}
