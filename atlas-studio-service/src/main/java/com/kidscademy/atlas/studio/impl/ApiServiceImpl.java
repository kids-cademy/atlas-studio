package com.kidscademy.atlas.studio.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.apiservice.client.Wikipedia;
import com.kidscademy.atlas.studio.ApiService;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.FeatureMeta;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.Taxon;
import com.kidscademy.atlas.studio.util.Strings;
import com.kidscademy.atlas.studio.www.CambridgeDictionary;
import com.kidscademy.atlas.studio.www.MerriamWebster;
import com.kidscademy.atlas.studio.www.PlantVillage;
import com.kidscademy.atlas.studio.www.SoftSchools;
import com.kidscademy.atlas.studio.www.TheFreeDictionary;
import com.kidscademy.atlas.studio.www.WikipediaArticleText;

import js.tiny.container.core.AppContext;

public class ApiServiceImpl implements ApiService {
    private final AppContext context;
    private final AtlasDao atlasDao;

    private final SoftSchools softSchools;
    private final TheFreeDictionary freeDictionary;
    private final CambridgeDictionary cambridgeDictionary;
    private final MerriamWebster merriamWebster;
    private final Wikipedia wikipedia;

    public ApiServiceImpl(AppContext context) throws IOException {
	this.context = context;
	this.atlasDao = context.getInstance(AtlasDao.class);

	this.softSchools = context.getInstance(SoftSchools.class);
	this.freeDictionary = context.getInstance(TheFreeDictionary.class);
	this.cambridgeDictionary = context.getInstance(CambridgeDictionary.class);
	this.merriamWebster = context.getInstance(MerriamWebster.class);
	this.wikipedia = context.getInstance(Wikipedia.class);
    }

    @Override
    public String getDefinition(Link link) {
	switch (link.getDomain()) {
	case "wikipedia.org":
	    return wikipedia.getDefinitions(link.getBasename()).get(0).getDefinition();

	case "thefreedictionary.com":
	    return freeDictionary.getDefinition(link.getBasename());

	case "cambridge.org":
	    return cambridgeDictionary.getDefinition(link.getBasename());

	case "merriam-webster.com":
	    return merriamWebster.getDefinition(link.getBasename());

	default:
	    return null;
	}
    }

    @Override
    public LinkedHashMap<String, String> getDescription(Link link) {
	LinkedHashMap<String, String> sections = new LinkedHashMap<>();
	switch (link.getDomain()) {
	case "softschools.com":
	    sections.put("softschools", Strings
		    .join(Strings.breakSentences(softSchools.getFacts(link.getPath()).getDescription()), "\r\n\r\n"));
	    break;

	case "wikipedia.org":
	    WikipediaArticleText article = new WikipediaArticleText(link.getUrl());
	    sections.put("wikipedia", article.getText());
	    break;

	case "psu.edu":
	    PlantVillage plantVillage = context.getInstance(PlantVillage.class);
	    plantVillage.getArticle(link.getPath()).getSections(sections);
	    break;

	default:
	    break;
	}
	return sections;
    }

    @Override
    public Map<String, String> getFacts(Link link) {
	switch (link.getDomain()) {
	case "softschools.com":
	    Map<String, String> facts = new HashMap<>();
	    for (String fact : softSchools.getFacts(link.getPath()).getFacts()) {
		facts.put(Strings.excerpt(fact), fact);
	    }
	    return facts;

	default:
	    return null;
	}
    }

    @Override
    public List<Taxon> getTaxonomy(Link link) {
	LinkedHashMap<String, String> taxonomy = null;
	switch (link.getDomain()) {
	case "wikipedia.org":
	    taxonomy = wikipedia.getTaxonomy(link.getBasename());
	    break;

	default:
	    return null;
	}

	List<Taxon> taxaList = new ArrayList<>();
	for (Map.Entry<String, String> taxon : taxonomy.entrySet()) {
	    taxaList.add(new Taxon(taxon.getKey(), taxon.getValue()));
	}
	return taxaList;
    }

    @Override
    public List<Feature> getFeatures(int collectionId, Link link) {
	Map<String, Double> values = null;
	switch (link.getDomain()) {
	case "wikipedia.org":
	    values = wikipedia.getEdibleNutrients(link.getBasename());
	    break;

	default:
	    return null;
	}

	// TODO: get collection ID from link meta and remove method parameter
	// int collectionId = link.getMeta().getCollectionId();

	List<Feature> features = new ArrayList<>();
	for (FeatureMeta meta : atlasDao.getCollectionFeaturesMeta(collectionId)) {
	    // to avoid full scan we can create a name resolver with hash map
	    // but at current sizes is not really helping
	    for (String label : values.keySet()) {
		// all feature meta names related to nutrients have at least one dot
		// if not used dot on name scanning there is confusion between 'saturated' and
		// 'monounsaturated' because both ends with 'saturated'
		if (meta.getName().endsWith("." + Strings.toDotCase(label))) {
		    Feature feature = new Feature(meta, values.get(label));
		    feature.postLoad();
		    features.add(feature);
		    break;
		}
	    }
	}
	return features;
    }
}
