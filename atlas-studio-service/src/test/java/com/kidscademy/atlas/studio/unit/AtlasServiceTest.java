package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kidscademy.apiservice.client.Wikipedia;
import com.kidscademy.apiservice.model.LifeForm;
import com.kidscademy.atlas.studio.AtlasService;
import com.kidscademy.atlas.studio.BusinessRules;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.impl.AtlasServiceImpl;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.ConservationStatus;
import com.kidscademy.atlas.studio.model.ExternalSource;
import com.kidscademy.atlas.studio.model.LinkSource;

import js.json.Json;
import js.rmi.BusinessException;
import js.tiny.container.core.AppContext;
import js.util.Classes;

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
	when(context.getInstance(Wikipedia.class)).thenReturn(wikipedia);
	when(context.getInstance(BusinessRules.class)).thenReturn(businessRules);
	service = new AtlasServiceImpl(context);
    }

    @Test
    public void getLinkDefinition_toLowerCase() throws MalformedURLException {
	URL link = new URL("https://en.wikipedia.org/wiki/Sperm_whale");
	String display = "Sperm Whale";
	ExternalSource externalSource = new ExternalSource(2, "https://en.wikipedia.org/wiki/", "Wikipedia article about ${display}", "definition,description,features,taxonomy");
	String definition = externalSource.getLinkDefinition(link, display);

	assertThat(definition, notNullValue());
	assertThat(definition, equalTo("Wikipedia article about Sperm Whale"));
    }

    @Test
    public void getLinkDefinition_getYouTubeTitle() throws MalformedURLException {
	URL link = new URL("https://www.youtube.com/watch?v=DT-TDegf-Xo");
	String display = "Sperm Whale";
	ExternalSource externalSource = new ExternalSource(1, "https://www.youtube.com/", "${API.getYouTubeTitle}.", "");
	String definition = externalSource.getLinkDefinition(link, display);

	assertThat(definition, notNullValue());
	assertThat(definition, equalTo("Sperm Whales Dealing : With The Unexpected - Wildlife Documentary."));
    }

    @Test
    public void importWikipediaObject() throws IOException, BusinessException {
	when(atlasDao.getCollectionById(2)).thenReturn(new AtlasCollection(2, "wild-birds"));
	
	ExternalSource externalSource = new ExternalSource(1, "https://en.wikipedia.org/wiki/",
		"Wikipedia article about ${display}", "definition,description,features,taxonomy");
	when(atlasDao.getExternalSourceByDomain("wikipedia.org")).thenReturn(externalSource);
	when(atlasDao.getLinkSourceByDomain(2, "wikipedia.org")).thenReturn(new LinkSource(1, externalSource));

	Json json = Classes.loadService(Json.class);
	LifeForm lifeForm = json.parse(Classes.getResourceAsReader("common-raven.json"), LifeForm.class);
	when(wikipedia.getLifeForm("Common_raven")).thenReturn(lifeForm);

	URL articleURL = new URL("https://en.wikipedia.org/wiki/Common_raven");
	service.importWikipediaObject(2, articleURL);

	ArgumentCaptor<AtlasObject> objectCaptor = ArgumentCaptor.forClass(AtlasObject.class);
	verify(atlasDao).saveAtlasObject(objectCaptor.capture());

	AtlasObject object = objectCaptor.getValue();
	assertThat(object, notNullValue());
	assertThat(object.getState(), equalTo(AtlasObject.State.CREATED));
	assertThat(object.getStartDate(), nullValue());
	assertThat(object.getEndDate(), nullValue());

	assertThat(object.getName(), notNullValue());
	assertThat(object.getName(), equalTo("corvus-corax"));

	assertThat(object.getDisplay(), notNullValue());
	assertThat(object.getDisplay(), equalTo("Common raven"));

	assertThat(object.getDescription(), notNullValue());
	assertThat(object.getDescription(), startsWith("<text><section name=\"wikipedia\"><p>The common raven"));

	assertThat(object.getConservation(), notNullValue());
	assertThat(object.getConservation(), equalTo(ConservationStatus.LC));

	assertThat(object.getTaxonomy(), notNullValue());
	assertThat(object.getTaxonomy(), hasSize(7));
	assertThat(object.getTaxonomy().get(0), notNullValue());
	assertThat(object.getTaxonomy().get(0).getName(), equalTo("kingdom"));
	assertThat(object.getTaxonomy().get(0).getValue(), equalTo("Animalia"));
	assertThat(object.getTaxonomy().get(1), notNullValue());
	assertThat(object.getTaxonomy().get(1).getName(), equalTo("phylum"));
	assertThat(object.getTaxonomy().get(1).getValue(), equalTo("Chordata"));
	assertThat(object.getTaxonomy().get(2), notNullValue());
	assertThat(object.getTaxonomy().get(2).getName(), equalTo("class"));
	assertThat(object.getTaxonomy().get(2).getValue(), equalTo("Aves"));
	assertThat(object.getTaxonomy().get(3), notNullValue());
	assertThat(object.getTaxonomy().get(3).getName(), equalTo("order"));
	assertThat(object.getTaxonomy().get(3).getValue(), equalTo("Passeriformes"));
	assertThat(object.getTaxonomy().get(4), notNullValue());
	assertThat(object.getTaxonomy().get(4).getName(), equalTo("family"));
	assertThat(object.getTaxonomy().get(4).getValue(), equalTo("Corvidae"));
	assertThat(object.getTaxonomy().get(5), notNullValue());
	assertThat(object.getTaxonomy().get(5).getName(), equalTo("genus"));
	assertThat(object.getTaxonomy().get(5).getValue(), equalTo("Corvus"));
	assertThat(object.getTaxonomy().get(6), notNullValue());
	assertThat(object.getTaxonomy().get(6).getName(), equalTo("species"));
	assertThat(object.getTaxonomy().get(6).getValue(), equalTo("Corax"));

	assertThat(object.getLinks(), notNullValue());
	assertThat(object.getLinks(), hasSize(1));
	assertThat(object.getLinks().get(0).getUrl(), notNullValue());
	assertThat(object.getLinks().get(0).getUrl(), equalTo(articleURL));
	assertThat(object.getLinks().get(0).getDefinition(), notNullValue());
	assertThat(object.getLinks().get(0).getDefinition(), equalTo("Wikipedia article about Common raven"));
    }
}
