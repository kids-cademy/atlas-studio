package com.kidscademy.atlas.studio.api;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import js.util.Classes;
import js.util.Strings;

public class GoogleTranslateTest
{
  @BeforeClass
  public static void beforeClass() throws IOException {
    // prepare GOOGLE_API_KEY system property for translate client options initialization
    // file translate-api-key should be created on test resources with content a valid API KEY
    System.setProperty("GOOGLE_API_KEY", Classes.getResourceAsString("translate-api-key"));
  }

  private Translate translate;

  @Before
  public void beforeTest() {
    // client library options loads API KEY from system property GOOGLE_API_KEY
    translate = TranslateOptions.getDefaultInstance().getService();
  }

  @Test
  public void helloWorld() throws IOException {
    Translation translation = translate.translate("Hello World!", TranslateOption.sourceLanguage("EN"), TranslateOption.targetLanguage("RO"));
    assertThat(translation.getTranslatedText(), equalTo("Salut Lume!"));
  }

  @Test
  public void helloWorld_FR() throws IOException {
    Translation translation = translate.translate("Hello World!", TranslateOption.sourceLanguage("EN"), TranslateOption.targetLanguage("FR"));
    assertThat(translation.getTranslatedText(), equalTo("Bonjour le monde!"));
  }

  @Test
  public void helloWorld_Model() throws IOException {
    // nmt : NMT - Neural Machine Translation
    // base: PBMT - Phrase-Based Machine Translation
    String model = "nmt";

    Translation translation = translate.translate("Hello World!", TranslateOption.sourceLanguage("EN"), TranslateOption.targetLanguage("RO"), TranslateOption.model(model));
    assertThat(translation.getTranslatedText(), equalTo("Salut Lume!"));
  }

  @Test
  public void htmlArticle() throws IOException {
    String article = Classes.getResourceAsString("american-goldfinch_description.htm");
    Translation translation = translate.translate(article, TranslateOption.sourceLanguage("EN"), TranslateOption.targetLanguage("RO"));

    Strings.save(translation.getTranslatedText(), new File("d://tmp//american-goldfinch_description_ro.htm"));
  }
}
