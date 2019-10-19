package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.kidscademy.atlas.studio.www.TheFreeDictionary;

import js.dom.Document;
import js.dom.DocumentBuilder;
import js.dom.Element;
import js.tiny.container.core.Factory;
import js.tiny.container.unit.TestContext;
import js.util.Classes;
import js.util.Files;

public class TheFreeDictionaryTest {
    private static final String DESCRIPTOR = "" + //
	    "<app-descriptor>" + //
	    "	<managed-classes>" + //
	    "		<the-free-dictionary interface='com.kidscademy.atlas.studio.www.TheFreeDictionary' type='REMOTE' url='https:xpath://www.thefreedictionary.com/' />"
	    + //
	    "	</managed-classes>" + //
	    "</app-descriptor>";

    private TheFreeDictionary dictionary;

    @Before
    public void beforeTest() throws Exception {
	TestContext.start(DESCRIPTOR);
	dictionary = Factory.getInstance(TheFreeDictionary.class);
    }

    public void concept() throws IOException {
	URL url = new URL("https://www.thefreedictionary.com/accordion");

	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	connection.setRequestProperty("User-Agent",
		"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");

	int responseCode = connection.getResponseCode();
	InputStream stream = responseCode == 200 ? connection.getInputStream() : connection.getErrorStream();

	Files.copy(stream, new File("d://tmp/accordion.page.xml"));

	// Files.dump(stream);

	// assertThat(responseCode, equalTo(200));
    }

    public void loadDefinition() throws FileNotFoundException {
	DocumentBuilder builder = Classes.loadService(DocumentBuilder.class);
	Document document = builder.loadHTML(new File("d://tmp/accordion.page.xml"));
	Element element = document.getByXPath("//*[@id='Definition']/SECTION[1]/DIV[1]/DIV");
	//Element element = document.getByXPath("/HTML/BODY/DIV[2]/DIV/DIV/MAIN/DIV[1]/DIV/DIV/DIV[2]/DIV[1]/SECTION[1]/DIV[1]/DIV");
	//Element element = document.getByXPath("//HTML/BODY");
	System.out.println(element.getText());
    }

    @Test
    public void getDefinition() {
	String definition = dictionary.getDefinition("accordion");
	assertThat(definition, notNullValue());
	assertThat(definition, startsWith("A portable wind instrument with a small keyboard and free metal reeds"));
    }
}
