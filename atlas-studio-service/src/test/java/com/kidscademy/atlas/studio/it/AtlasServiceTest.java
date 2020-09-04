package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.kidscademy.atlas.studio.model.ExternalSource;

public class AtlasServiceTest
{
  @Test
  public void getLinkDefinition_getYouTubeTitle() throws MalformedURLException {
    URL link = new URL("https://www.youtube.com/watch?v=DT-TDegf-Xo");
    String display = "Sperm Whale";
    ExternalSource externalSource = new ExternalSource(1, "https://www.youtube.com/", "${API.getYouTubeTitle}.", "");
    String definition = externalSource.getLinkDefinition(link, display);

    assertThat(definition, notNullValue());
    assertThat(definition, equalTo("Sperm Whales Dealing : With The Unexpected - Wildlife Documentary."));
  }
}
