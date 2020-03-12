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

import com.kidscademy.atlas.studio.AtlasService;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.impl.AtlasServiceImpl;
import com.kidscademy.atlas.studio.model.LinkMeta;

import js.tiny.container.core.AppContext;

@RunWith(MockitoJUnitRunner.class)
public class AtlasServiceTest {
    @Mock
    private AppContext context;
    @Mock
    private AtlasDao atlasDao;

    private AtlasService service;

    @Before
    public void beforeTest() throws IOException {
	when(context.getInstance(AtlasDao.class)).thenReturn(atlasDao);
	service = new AtlasServiceImpl(context);
    }

    @Test
    public void getLinkDefinition_toLowerCase() throws MalformedURLException {
	URL link = new URL("https://en.wikipedia.org/wiki/Sperm_whale");
	String display = "Sperm Whale";
	LinkMeta linkMeta = new LinkMeta("wikipedia.org", display, "Wikipedia article about ${display.toLowerCase}.");

	when(atlasDao.getLinkMetaByDomain("wikipedia.org")).thenReturn(linkMeta);
	String definition = service.getLinkDefinition(link, display);
	
	assertThat(definition, notNullValue());
	assertThat(definition, equalTo("Wikipedia article about sperm whale."));
    }

    @Test
    public void getLinkDefinition_getYouTubeTitle() throws MalformedURLException {
	URL link = new URL("https://www.youtube.com/watch?v=DT-TDegf-Xo");
	String display = "Sperm Whale";
	LinkMeta linkMeta = new LinkMeta("youtube.com", display, "${API.getYouTubeTitle}.");

	when(atlasDao.getLinkMetaByDomain("youtube.com")).thenReturn(linkMeta);
	String definition = service.getLinkDefinition(link, display);
	
	assertThat(definition, notNullValue());
	assertThat(definition, equalTo("Sperm Whales Dealing : With The Unexpected - Wildlife Documentary."));
    }
}
