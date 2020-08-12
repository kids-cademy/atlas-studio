package com.kidscademy.atlas.studio.api;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import js.util.Classes;
import js.util.Strings;

public class GoogleTranslateTest {
    @Test
    public void helloWorld() throws IOException {
	// prepare GOOGLE_API_KEY system property for translate client options
	// initialization; file translate-api-key should be created on test resources
	// with content a valid API KEY
	System.setProperty("GOOGLE_API_KEY", Strings.load(Classes.getResourceAsReader("translate-api-key")));

	// client library options loads API KEY from system property GOOGLE_API_KEY
	Translate translate = TranslateOptions.getDefaultInstance().getService();

	// nmt : NMT - Neural Machine Translation
	// base: PBMT - Phrase-Based Machine Translation
	String model = "nmt";

	Translation translation = translate.translate("Hello World!", Translate.TranslateOption.sourceLanguage("EN"),
		Translate.TranslateOption.targetLanguage("RO"), Translate.TranslateOption.model(model));
	assertThat(translation.getTranslatedText(), equalTo("Salut Lume!"));
    }
}
