package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.kidscademy.atlas.studio.www.NationalGeographicArticle;

public class NationalGeographicTest {
    @Test
    public void loadArticle() throws MalformedURLException {
	NationalGeographicArticle article = new NationalGeographicArticle(
		new URL("https://www.nationalgeographic.com/animals/birds/p/peregrine-falcon/"));

	assertThat(article.getMinimumLifespan(), equalTo(536468184.0));
	assertThat(article.getMinimumWeight(), equalTo(0.5329704598287691));
	assertThat(article.getMaximumWeight(), equalTo(1.6017463287407154));
	assertThat(article.getMinimumLength(), equalTo(0.35559999999999997));
	assertThat(article.getMaximumLength(), equalTo(0.4826));
	assertThat(article.getMinimumWingspan(), equalTo(1.005839967813121));
	assertThat(article.getMaximumWingspan(), equalTo(1.097279964887041));
    }
}
