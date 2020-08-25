package com.kidscademy.atlas.studio.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.kidscademy.atlas.studio.CT;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.TranslationKey.Discriminator;

import js.util.Strings;

public class Translator
{
  public static final String DEFAULT_LANGUAGE = "EN";

  public static Translator getDefaultInstance() {
    return new Translator(DEFAULT_LANGUAGE);
  }

  public static boolean isDefaultLanguage(String language) {
    return DEFAULT_LANGUAGE.equals(language);
  }

  private static Translate translate;
  private static final Object translateLock = new Object();

  private final AtlasDao dao;
  private final String language;

  private Translator(String language) {
    this(null, language);
  }

  public Translator(AtlasDao dao, String language) {
    this.dao = dao;
    this.language = language;

    if(translate == null) {
      synchronized(translateLock) {
        if(translate == null) {
          // google translate client library loads API KEY from system property GOOGLE_API_KEY
          if(CT.gooleApiKey() != null) {
            System.setProperty("GOOGLE_API_KEY", CT.gooleApiKey());
          }
          translate = TranslateOptions.getDefaultInstance().getService();
        }
      }
    }
  }

  public boolean isDefaultLanguage() {
    return DEFAULT_LANGUAGE.equals(language);
  }

  public String getLanguage() {
    return language;
  }

  public void saveAtlasObjectDisplay(int objectId, String display) {
    saveTranslation(Discriminator.OBJECT_DISPLAY, objectId, language, display);
  }

  public void saveAtlasObjectAliases(int objectId, List<String> aliases) {
    saveTranslation(Discriminator.OBJECT_ALIASES, objectId, language, Strings.join(aliases, ','));
  }

  public void saveAtlasObjectDefinition(int objectId, String definition) {
    saveTranslation(Discriminator.OBJECT_DEFINITION, objectId, language, definition);
  }

  public void saveAtlasObjectDescription(int objectId, String description) {
    saveTranslation(Discriminator.OBJECT_DESCRIPTION, objectId, language, description);
  }

  public void saveAtlasObjectSampleTitle(int objectId, String sampleTitle) {
    saveTranslation(Discriminator.OBJECT_SAMPLE_TITLE, objectId, language, sampleTitle);
  }

  public void saveFactTitle(int factId, String title) {
    saveTranslation(Discriminator.FACT_TITLE, factId, language, title);
  }

  public void saveFactText(int factId, String text) {
    saveTranslation(Discriminator.FACT_TEXT, factId, language, text);
  }

  public void saveFeatureMetaDisplay(int featureMetaId, String display) {
    saveTranslation(Discriminator.FEATURE_META_DISPLAY, featureMetaId, language, display);
  }

  private void saveTranslation(Discriminator discriminator, int objectId, String language, String text) {
    if(text == null || text.isEmpty()) {
      dao.removeTranslation(new TranslationKey(discriminator, objectId, language));
      return;
    }

    TranslationData translation = new TranslationData();
    translation.setDiscriminator(discriminator);
    translation.setObjectId(objectId);
    translation.setLanguage(language);
    translation.setText(text);
    dao.saveTranslation(translation);
  }

  public String getAtlasObjectDisplay(int objectId) {
    return dao.getTranslation(new TranslationKey(Discriminator.OBJECT_DISPLAY, objectId, language));
  }

  private static final List<String> EMPTY_STRINGS = new ArrayList<>(0);

  public List<String> getAtlasObjectAliases(int objectId) {
    String aliases = dao.getTranslation(new TranslationKey(Discriminator.OBJECT_ALIASES, objectId, language));
    return aliases != null ? Strings.split(aliases, ',') : EMPTY_STRINGS;
  }

  public String getAtlasObjectSampleTitle(int objectId) {
    return dao.getTranslation(new TranslationKey(Discriminator.OBJECT_SAMPLE_TITLE, objectId, language));
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

  public String getFactTitle(int factId) {
    return dao.getTranslation(new TranslationKey(Discriminator.FACT_TITLE, factId, language));
  }

  public String getFactText(int factId) {
    return dao.getTranslation(new TranslationKey(Discriminator.FACT_TEXT, factId, language));
  }

  public String getFeatureMetaDisplay(int featureMetaId) {
    return dao.getTranslation(new TranslationKey(Discriminator.FEATURE_META_DISPLAY, featureMetaId, language));
  }

  public String getFeatureValue(Feature feature) {
    TranslationData translation = dao.getTranslationData(new TranslationKey(Discriminator.FEATURE_VALUE, feature.getId(), language));
    if(translation == null) {
      translation = new TranslationData(Discriminator.FEATURE_VALUE, feature.getId(), language);
    }
    if(translation.getTimestamp().before(feature.getTimestamp())) {
      translation.setTimestamp(new Date());
      translation.setText(translate(feature.getDisplay(), language));
      dao.saveTranslation(translation);
    }
    return translation.getText();
  }

  private static String translate(String text, String language) {
    if(text == null) {
      return null;
    }
    Translation translation = translate.translate(text, TranslateOption.sourceLanguage(Translator.DEFAULT_LANGUAGE), TranslateOption.targetLanguage(language));
    return translation.getTranslatedText();
  }
}
