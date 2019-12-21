package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kidscademy.atlas.studio.AtlasService;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.dao.TaxonomyDao;
import com.kidscademy.atlas.studio.impl.AtlasServiceImpl;
import com.kidscademy.atlas.studio.model.AtlasItem;

import js.dom.DocumentBuilder;
import js.tiny.container.core.AppContext;
import js.tools.commons.util.Classes;

@RunWith(MockitoJUnitRunner.class)
public class AtlasServiceTest {
    @Mock
    private AppContext context;
    @Mock
    private AtlasDao atlasDao;
    @Mock
    private TaxonomyDao taxonomyDao;
    
    private AtlasService service;

    @Before
    public void beforeTest() throws IOException {
	when(context.getAppFile(anyString())).thenReturn(new File("fixture/fake-file"));
	when(context.loadService(DocumentBuilder.class)).thenReturn(Classes.loadService(DocumentBuilder.class));
	when(context.getInstance(AtlasDao.class)).thenReturn(atlasDao);
	when(context.getInstance(TaxonomyDao.class)).thenReturn(taxonomyDao);
	service = new AtlasServiceImpl(context);
    }

    @Test
    public void importWikipediaObject() throws IOException {
	URL articleURL = new URL("https://en.wikipedia.org/wiki/Common_raven");
	AtlasItem object = service.importWikipediaObject(2, articleURL);
	
	assertThat(object, notNullValue());
	
	assertThat(object.getName(), notNullValue());
	assertThat(object.getName(), equalTo("Corvus corax"));
	
	assertThat(object.getDisplay(), notNullValue());
	assertThat(object.getDisplay(), equalTo("Common raven"));

//	assertThat(object.getDescription(), notNullValue());
//	assertThat(object.getDescription(), startsWith("<p>The common raven"));
//
//	assertThat(object.getConservation(), notNullValue());
//	assertThat(object.getConservation(), equalTo(ConservationStatus.LC));
	
	//assertThat(object.getTaxonomy(), notNullValue());

//	assertThat(object.getLinks(), notNullValue());
//	assertThat(object.getLinks(), hasSize(1));
//	assertThat(object.getLinks().get(0).getUrl(), notNullValue());
//	assertThat(object.getLinks().get(0).getUrl(), equalTo(articleURL));
//	assertThat(object.getLinks().get(0).getDescription(), notNullValue());
//	assertThat(object.getLinks().get(0).getDescription(), equalTo("Wikipedia article about common raven."));
    }
}
