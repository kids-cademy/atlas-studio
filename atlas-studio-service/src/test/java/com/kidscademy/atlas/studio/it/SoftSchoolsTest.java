package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kidscademy.atlas.studio.www.SoftSchools;
import com.kidscademy.atlas.studio.www.SoftSchoolsFacts;

import js.core.AppContext;
import js.unit.TestContext;

public class SoftSchoolsTest {
    private static final String DESCRIPTOR = "" + //
	    "<app-descriptor>" + //
	    " <managed-classes>" + //
	    "  <soft-schools interface='com.kidscademy.atlas.studio.www.SoftSchools' type='REMOTE' url='http:xpath://www.softschools.com/' />"
	    + //
	    " </managed-classes>" + //
	    "</app-descriptor>";

    private SoftSchools softSchools;

    @Before
    public void beforeTest() throws Exception {
	AppContext context = TestContext.start(DESCRIPTOR);
	softSchools = context.getInstance(SoftSchools.class);
    }

    @Test
    public void getDescription() {
	String description = softSchools.getDescription("facts/animals/lion_facts/2/");
	assertThat(description, notNullValue());
	assertThat(description, startsWith("Lions are one of the largest cats in the world."));
    }

    @Test
    public void getFacts() {
	SoftSchoolsFacts facts = softSchools.getFacts("facts/animals/lion_facts/2/");
	assertThat(facts, notNullValue());
	assertThat(facts.getTitle(), notNullValue());
	assertThat(facts.getTitle(), equalTo("Lion Facts"));
	assertThat(facts.getDescription(), notNullValue());
	assertThat(facts.getDescription(), startsWith("Lions are one of the largest cats in the world."));
	assertThat(facts.getFacts(), notNullValue());
	assertThat(facts.getFacts(), hasSize(15));
	assertThat(facts.getFacts().get(0), startsWith("Lions are carnivores and they hunt mostly antelopes"));
    }

    @Test
    public void getFactLinks() {
	List<String> links = softSchools.getFactLinks("facts/animals");
	assertThat(links, notNullValue());
	assertThat(links, hasSize(758));

	for (String link : links) {
	    System.out.println(link);
	}
    }
}
