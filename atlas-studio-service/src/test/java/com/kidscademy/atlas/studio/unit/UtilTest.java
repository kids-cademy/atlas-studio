package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.kidscademy.atlas.studio.util.Files;

public class UtilTest {
    @Test
    public void Files_copyTemplate() throws IOException {
	Map<String, String> variables = new HashMap<>();
	variables.put("app-name", "App");
	variables.put("app-logotype", "Logotype");
	variables.put("app-definition", "Definition.");
	variables.put("publisher", "Publisher");
	variables.put("edition", "Edition");
	variables.put("version-name", "Version Name");
	variables.put("update-date", "Update Date");
	variables.put("license", "License");

	Writer writer = new StringWriter();
	Files.copy("strings.xml", variables, writer);

	String result = writer.toString();
	assertThat(result, notNullValue());
	assertThat(result, containsString("<string name=\"app_name\">App</string>"));
	assertThat(result, containsString("<string name=\"app_logotype\">Logotype</string>"));
	assertThat(result, containsString("<string name=\"app_description\">Definition.</string>"));
	assertThat(result, containsString("<string name=\"app_publisher\">Published by Publisher</string>"));
	assertThat(result, containsString("<string name=\"app_edition\">Edition</string>"));
	assertThat(result, containsString("<string name=\"app_release\">Release Version Name</string>"));
	assertThat(result, containsString("<string name=\"app_update\">Update Date</string>"));
	assertThat(result, containsString("<string name=\"content_license\">License</string>"));
	assertThat(result, containsString("<string name=\"app_about\">Definition.</string>"));
	assertThat(result, containsString("<string name=\"app_about_caption\">About App</string>"));
	assertThat(result, containsString("<string name=\"app_rate_caption\">Rate App</string>"));
	assertThat(result, containsString("<string name=\"app_share_caption\">Share App</string>"));
    }
}
