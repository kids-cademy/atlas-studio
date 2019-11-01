package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.kidscademy.atlas.studio.www.WikiHow;

import js.tiny.container.core.AppContext;
import js.tiny.container.unit.TestContext;

public class WikiHowTest {
    private static final String DESCRIPTOR = "" + //
	    "<app-descriptor>" + //
	    " <managed-classes>" + //
	    "  <soft-schools interface='com.kidscademy.atlas.studio.www.WikiHow' type='REMOTE' url='https:xpath://www.wikihow.com/' />"
	    + //
	    " </managed-classes>" + //
	    "</app-descriptor>";

    private WikiHow wikiHow;

    @Before
    public void beforeTest() throws Exception {
	AppContext context = TestContext.start(DESCRIPTOR);
	wikiHow = context.getInstance(WikiHow.class);
    }

    @Test
    public void getDescription() {
	String title = wikiHow.getTitle("Play-the-Trumpet");
	assertThat(title, notNullValue());
	assertThat(title, startsWith("How to Play the Trumpet"));
    }
}
