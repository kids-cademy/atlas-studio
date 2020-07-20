package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kidscademy.apiservice.client.Wikipedia;
import com.kidscademy.atlas.studio.AtlasService;
import com.kidscademy.atlas.studio.BusinessRules;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.impl.AtlasServiceImpl;
import com.kidscademy.atlas.studio.model.ExternalSource;

import js.tiny.container.core.AppContext;

@RunWith(MockitoJUnitRunner.class)
public class AtlasServiceTest {
    @Mock
    private AppContext context;
    @Mock
    private AtlasDao atlasDao;
    @Mock
    private Wikipedia wikipedia;
    @Mock
    private BusinessRules businessRules;

    private AtlasService service;

    @Before
    public void beforeTest() throws IOException {
	when(context.getInstance(AtlasDao.class)).thenReturn(atlasDao);
	//when(context.getInstance(Wikipedia.class)).thenReturn(wikipedia);
	when(context.getInstance(BusinessRules.class)).thenReturn(businessRules);
	service = new AtlasServiceImpl(context);
    }

    @Test
    public void getLinkDefinition_toLowerCase() throws MalformedURLException {
	URL link = new URL("https://en.wikipedia.org/wiki/Sperm_whale");
	String display = "Sperm Whale";
	ExternalSource externalSource = new ExternalSource(2, "https://en.wikipedia.org/wiki/",
		"Wikipedia article about ${display}", "definition,description,features,taxonomy");
	String definition = externalSource.getLinkDefinition(link, display);

	assertThat(definition, notNullValue());
	assertThat(definition, equalTo("Wikipedia article about Sperm Whale"));
    }

    @Test
    public void getLinkDefinition_getYouTubeTitle() throws MalformedURLException {
	URL link = new URL("https://www.youtube.com/watch?v=DT-TDegf-Xo");
	String display = "Sperm Whale";
	ExternalSource externalSource = new ExternalSource(1, "https://www.youtube.com/", "${API.getYouTubeTitle}.",
		"");
	String definition = externalSource.getLinkDefinition(link, display);

	assertThat(definition, notNullValue());
	assertThat(definition, equalTo("Sperm Whales Dealing : With The Unexpected - Wildlife Documentary."));
    }
}
