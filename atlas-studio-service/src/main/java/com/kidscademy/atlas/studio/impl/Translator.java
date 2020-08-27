package com.kidscademy.atlas.studio.impl;

import java.util.Date;
import java.util.List;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.kidscademy.atlas.studio.CT;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.TranslationData;
import com.kidscademy.atlas.studio.model.TranslationKey;
import com.kidscademy.atlas.studio.model.TranslationKey.Discriminator;
import com.kidscademy.atlas.studio.util.Strings;

import js.log.Log;
import js.log.LogFactory;

/**
 * Container for translation related logic.
 * 
 * @author Iulian Rotaru
 */
public class Translator
{
  private static final Log log = LogFactory.getLog(Translator.class);

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

  // ----------------------------------------------------------------------------------------------

  public String getAtlasObjectDisplay(int objectId) {
    return dao.getTranslation(new TranslationKey(Discriminator.OBJECT_DISPLAY, objectId, language));
  }

  public void translateAtlasObjectDisplay(int objectId, String display) {
    final TranslationKey key = new TranslationKey(Discriminator.OBJECT_DISPLAY, objectId, language);
    if(!dao.hasTranslation(key)) {
      saveTranslation(key, translate(display));
    }
  }

  public void saveAtlasObjectDisplay(int objectId, String display) {
    saveTranslation(Discriminator.OBJECT_DISPLAY, objectId, language, display);
  }

  // ----------------------------------------------------------------------------------------------

  public List<String> getAtlasObjectAliases(int objectId) {
    String aliases = dao.getTranslation(new TranslationKey(Discriminator.OBJECT_ALIASES, objectId, language));
    return aliases != null ? Strings.split(aliases, ',') : CT.EMPTY_STRINGS;
  }

  public void translateAtlasObjectAliases(int objectId, List<String> aliases) {
    final TranslationKey key = new TranslationKey(Discriminator.OBJECT_ALIASES, objectId, language);
    if(!dao.hasTranslation(key)) {
      saveTranslation(key, translate(Strings.join(aliases, ',')));
    }
  }

  public void saveAtlasObjectAliases(int objectId, List<String> aliases) {
    saveTranslation(Discriminator.OBJECT_ALIASES, objectId, language, Strings.join(aliases, ','));
  }

  // ----------------------------------------------------------------------------------------------

  public String getAtlasObjectDefinition(int objectId) {
    return dao.getTranslation(new TranslationKey(Discriminator.OBJECT_DEFINITION, objectId, language));
  }

  public void translateAtlasObjectDefinition(int objectId, String definition) {
    final TranslationKey key = new TranslationKey(Discriminator.OBJECT_DEFINITION, objectId, language);
    if(!dao.hasTranslation(key)) {
      saveTranslation(key, translate(definition));
    }
  }

  public void saveAtlasObjectDefinition(int objectId, String definition) {
    saveTranslation(Discriminator.OBJECT_DEFINITION, objectId, language, definition);
  }

  // ----------------------------------------------------------------------------------------------

  public String getAtlasObjectDescription(int objectId) {
    return dao.getTranslation(new TranslationKey(Discriminator.OBJECT_DESCRIPTION, objectId, language));
  }

  public void translateAtlasObjectDescription(int objectId, String description) {
    final TranslationKey key = new TranslationKey(Discriminator.OBJECT_DESCRIPTION, objectId, language);
    if(!dao.hasTranslation(key)) {
      saveTranslation(key, translate(description));
    }
  }

  public void saveAtlasObjectDescription(int objectId, String description) {
    saveTranslation(Discriminator.OBJECT_DESCRIPTION, objectId, language, description);
  }

  // ----------------------------------------------------------------------------------------------

  public String getAtlasObjectSampleTitle(int objectId) {
    return dao.getTranslation(new TranslationKey(Discriminator.OBJECT_SAMPLE_TITLE, objectId, language));
  }

  public void translateAtlasObjectSampleTitle(int objectId, String sampleTitle) {
    final TranslationKey key = new TranslationKey(Discriminator.OBJECT_SAMPLE_TITLE, objectId, language);
    if(!dao.hasTranslation(key)) {
      saveTranslation(key, translate(sampleTitle));
    }
  }

  public void saveAtlasObjectSampleTitle(int objectId, String sampleTitle) {
    saveTranslation(Discriminator.OBJECT_SAMPLE_TITLE, objectId, language, sampleTitle);
  }

  // ----------------------------------------------------------------------------------------------

  public String getFactTitle(int factId) {
    return dao.getTranslation(new TranslationKey(Discriminator.FACT_TITLE, factId, language));
  }

  public void translateFactTitle(int factId, String title) {
    final TranslationKey key = new TranslationKey(Discriminator.FACT_TITLE, factId, language);
    if(!dao.hasTranslation(key)) {
      saveTranslation(key, translate(title));
    }
  }

  public void saveFactTitle(int factId, String title) {
    saveTranslation(Discriminator.FACT_TITLE, factId, language, title);
  }

  // ----------------------------------------------------------------------------------------------

  public String getFactText(int factId) {
    return dao.getTranslation(new TranslationKey(Discriminator.FACT_TEXT, factId, language));
  }

  public void translateFactText(int factId, String text) {
    final TranslationKey key = new TranslationKey(Discriminator.FACT_TEXT, factId, language);
    if(!dao.hasTranslation(key)) {
      saveTranslation(key, translate(text));
    }
  }

  public void saveFactText(int factId, String text) {
    saveTranslation(Discriminator.FACT_TEXT, factId, language, text);
  }

  // ----------------------------------------------------------------------------------------------

  public String getTaxonMetaDisplay(int taxonMetaId) {
    return dao.getTranslation(new TranslationKey(Discriminator.TAXON_META_DISPLAY, taxonMetaId, language));
  }

  // ----------------------------------------------------------------------------------------------

  public String getTaxonValue(int taxonId) {
    return dao.getTranslation(new TranslationKey(Discriminator.TAXON_VALUE, taxonId, language));
  }

  // ----------------------------------------------------------------------------------------------

  public String getFeatureMetaDisplay(int featureMetaId) {
    return dao.getTranslation(new TranslationKey(Discriminator.FEATURE_META_DISPLAY, featureMetaId, language));
  }

  public void translateFeatureMetaDisplay(int featureMetaId, String display) {
    final TranslationKey key = new TranslationKey(Discriminator.FEATURE_META_DISPLAY, featureMetaId, language);
    if(!dao.hasTranslation(key)) {
      saveTranslation(key, translate(display));
    }
  }

  public void saveFeatureMetaDisplay(int featureMetaId, String display) {
    saveTranslation(Discriminator.FEATURE_META_DISPLAY, featureMetaId, language, display);
  }

  // ----------------------------------------------------------------------------------------------

  public String getFeatureValue(Feature feature) {
    final TranslationKey key = new TranslationKey(Discriminator.FEATURE_VALUE, feature.getId(), language);
    TranslationData translation = dao.getTranslationData(key);
    if(translation == null) {
      translation = new TranslationData(key);
    }
    if(translation.getTimestamp().before(feature.getTimestamp())) {
      translation.setTimestamp(new Date());
      translation.setText(translate(feature.getDisplay()));
      dao.saveTranslation(translation);
    }
    return translation.getText();
  }

  // ----------------------------------------------------------------------------------------------

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

  private void saveTranslation(TranslationKey key, String text) {
    if(text == null || text.isEmpty()) {
      dao.removeTranslation(key);
      return;
    }
    TranslationData translation = new TranslationData(key);
    translation.setText(text);
    dao.saveTranslation(translation);
  }

  private String translate(String text) {
    if(text == null) {
      return null;
    }
    log.debug("Invoke translation service to |%s| with |%s|.", language, Strings.ellipsis(text, 40));
    Translation translation = translate.translate(text, TranslateOption.sourceLanguage(Translator.DEFAULT_LANGUAGE), TranslateOption.targetLanguage(language));
    return translation.getTranslatedText();
  }
}
