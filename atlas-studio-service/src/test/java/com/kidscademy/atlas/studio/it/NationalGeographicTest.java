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

	assertThat(article.getMinimumLifespan(), equalTo(0D));
	assertThat(article.getMaximumLifespan(), equalTo(536468184.0));
	assertThat(article.getMinimumWeight(), equalTo(0.53));
	assertThat(article.getMaximumWeight(), equalTo(1.6));
	assertThat(article.getMinimumLength(), equalTo(0.36));
	assertThat(article.getMaximumLength(), equalTo(0.48));
	assertThat(article.getMinimumWingspan(), equalTo(1.01));
	assertThat(article.getMaximumWingspan(), equalTo(1.1));
    }
}
