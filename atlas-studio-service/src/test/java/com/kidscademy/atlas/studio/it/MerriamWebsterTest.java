package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.kidscademy.atlas.studio.www.MerriamWebster;

import js.dom.Document;
import js.dom.DocumentBuilder;
import js.dom.Element;
import js.tiny.container.core.AppContext;
import js.tiny.container.unit.TestContext;
import js.tools.commons.util.Classes;

public class MerriamWebsterTest {
    private static final String DESCRIPTOR = "" + //
	    "<app-descriptor>" + //
	    " <managed-classes>" + //
	    "  <merriam-webster interface='com.kidscademy.atlas.studio.www.MerriamWebster' type='REMOTE' url='https:xpath://www.merriam-webster.com/dictionary/'/>"
	    + //
	    " </managed-classes>" + //
	    "</app-descriptor>";

    private MerriamWebster webster;

    @Before
    public void beforeTest() throws Exception {
	AppContext context = TestContext.start(DESCRIPTOR);
	webster = context.getInstance(MerriamWebster.class);
    }

    @Test
    public void getDefinition() {
	String definition = webster.getDefinition("dulcitone");
	assertThat(definition, notNullValue());
	assertThat(definition, startsWith(": a keyboard instrument similar to the celesta"));
    }

    @Test
    public void definitionXPath() {
	DocumentBuilder builder = Classes.loadService(DocumentBuilder.class);
	Document document = builder.loadHTML(getClass().getResourceAsStream("/webster-response.xml"));
	Element element = document.getByXPath("//DIV[@id='dictionary-entry-1']//SPAN[@class='dtText']");
	assertThat(element, notNullValue());
	System.out.println(element.getText());
    }
}
