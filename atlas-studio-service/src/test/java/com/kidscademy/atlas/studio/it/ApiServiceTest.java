package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kidscademy.apiservice.client.Wikipedia;
import com.kidscademy.atlas.studio.ApiService;
import com.kidscademy.atlas.studio.BusinessRules;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.impl.ApiServiceImpl;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.ConservationStatus;
import com.kidscademy.atlas.studio.model.DescriptionMeta;
import com.kidscademy.atlas.studio.model.ExternalSource;
import com.kidscademy.atlas.studio.model.LinkSource;
import com.kidscademy.atlas.studio.model.TaxonMeta;

import js.rmi.BusinessException;
import js.tiny.container.core.AppContext;
import js.tiny.container.unit.TestContext;

@RunWith(MockitoJUnitRunner.class)
public class ApiServiceTest {
    private static final String DESCRIPTOR = "" //
	    + "<app-descriptor>" //
	    + "  <managed-classes>" //
	    + "    <wikipedia interface='com.kidscademy.apiservice.client.Wikipedia' type='REMOTE' url='http:rest://api.kids-cademy.com/wikipedia/' />" //
	    + "  </managed-classes>" //
	    + "</app-descriptor>";

    @Mock
    private AppContext context;
    @Mock
    private AtlasDao atlasDao;
    @Mock
    private BusinessRules businessRules;

    private ApiService service;

    @Before
    public void beforeTest() throws Exception {
	when(context.getInstance(AtlasDao.class)).thenReturn(atlasDao);

	Wikipedia wikipedia = TestContext.start(DESCRIPTOR).getInstance(Wikipedia.class);
	when(context.getInstance(Wikipedia.class)).thenReturn(wikipedia);

	service = new ApiServiceImpl(context);
    }

    @Test
    public void importWikipediaObject() throws IOException, BusinessException {
	AtlasCollection collection = new AtlasCollection(2, "wild-birds");
	collection.setDescriptionMeta(descriptionMeta());
	collection.setTaxonomyMeta(taxonomyMeta());
	when(atlasDao.getCollectionById(2)).thenReturn(collection);
	
	ExternalSource externalSource = new ExternalSource(1, "https://en.wikipedia.org/wiki/",
		"Wikipedia article about ${display}", "definition,description,features,taxonomy");
	when(atlasDao.getExternalSourceByDomain("wikipedia.org")).thenReturn(externalSource);
	when(atlasDao.getLinkSourceByDomain(2, "wikipedia.org")).thenReturn(new LinkSource(1, externalSource));

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
	assertThat(object.getDescription(), startsWith("<text><section name=\"description\"></section><section name=\"habitat\"></section><section name=\"wikipedia\"><p>The common raven"));

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

    private static List<DescriptionMeta> descriptionMeta() {
	List<DescriptionMeta> meta = new ArrayList<>();
	meta.add(new DescriptionMeta("description", "Description definition."));
	meta.add(new DescriptionMeta("habitat", "Habitat definition."));
	return meta;
    }

    private static List<TaxonMeta> taxonomyMeta() {
	List<TaxonMeta> meta = new ArrayList<>();
	meta.add(new TaxonMeta("kingdom"));
	meta.add(new TaxonMeta("phylum"));
	meta.add(new TaxonMeta("class"));
	meta.add(new TaxonMeta("order"));
	meta.add(new TaxonMeta("family"));
	meta.add(new TaxonMeta("genus"));
	meta.add(new TaxonMeta("species"));
	return meta;
    }
}
