package com.kidscademy.atlas.studio.unit;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.kidscademy.atlas.studio.model.Taxon;
import com.kidscademy.atlas.studio.www.LifeFormWikipediaArticle;

public class LifeFormWikipediaArticleTest {

    @Test
    public void parseDescription() throws IOException {
	LifeFormWikipediaArticle article = new LifeFormWikipediaArticle(
		new URL("https://en.wikipedia.org/wiki/Australasian_darter"));

	System.out.println(article.getScientificName());
	System.out.println(article.getCommonName());
	System.out.println(article.getStartDate());
	System.out.println(article.getEndDate());
	System.out.println(article.getConservationStatus());
	for (Taxon taxon : article.getTaxonomy()) {
	    System.out.println(taxon.getName() + ":" + taxon.getValue());
	}
	System.out.println(article.getDefinition());
	System.out.println(article.getDescription());
    }
}
