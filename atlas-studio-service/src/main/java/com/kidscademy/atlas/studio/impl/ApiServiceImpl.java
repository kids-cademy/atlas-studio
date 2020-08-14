package com.kidscademy.atlas.studio.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.apiservice.client.Animalia;
import com.kidscademy.apiservice.client.Wikipedia;
import com.kidscademy.apiservice.model.LifeForm;
import com.kidscademy.apiservice.model.PhysicalTrait;
import com.kidscademy.atlas.studio.ApiService;
import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.API;
import com.kidscademy.atlas.studio.model.ApiDescriptor;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.ConservationStatus;
import com.kidscademy.atlas.studio.model.DescriptionMeta;
import com.kidscademy.atlas.studio.model.ExternalSource;
import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.FeatureMeta;
import com.kidscademy.atlas.studio.model.HDate;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.LinkSource;
import com.kidscademy.atlas.studio.model.Taxon;
import com.kidscademy.atlas.studio.model.TaxonMeta;
import com.kidscademy.atlas.studio.util.Files;
import com.kidscademy.atlas.studio.util.Strings;
import com.kidscademy.atlas.studio.www.CambridgeDictionary;
import com.kidscademy.atlas.studio.www.MerriamWebster;
import com.kidscademy.atlas.studio.www.PlantVillage;
import com.kidscademy.atlas.studio.www.SoftSchools;
import com.kidscademy.atlas.studio.www.TheFreeDictionary;
import com.kidscademy.atlas.studio.www.WikipediaArticleText;

import js.lang.BugError;
import js.log.Log;
import js.log.LogFactory;
import js.tiny.container.core.AppContext;

public class ApiServiceImpl implements ApiService {
    private static final Log log = LogFactory.getLog(ApiService.class);

    private final List<ApiDescriptor> availableApis;
    private final Map<String, Method> apiMethods;

    private final AppContext context;
    private final AtlasDao atlasDao;

    private final SoftSchools softSchools;
    private final TheFreeDictionary freeDictionary;
    private final CambridgeDictionary cambridgeDictionary;
    private final MerriamWebster merriamWebster;

    private final Animalia animalia;
    private final Wikipedia wikipedia;

    public ApiServiceImpl(AppContext context) {
	this.availableApis = new ArrayList<>();
	this.apiMethods = new HashMap<>();
	for (Method method : getClass().getDeclaredMethods()) {
	    API api = method.getAnnotation(API.class);
	    if (api != null) {
		this.availableApis.add(new ApiDescriptor(api));
		method.setAccessible(true);
		this.apiMethods.put(api.name(), method);
	    }
	}
	Collections.sort(this.availableApis);

	this.context = context;
	this.atlasDao = context.getInstance(AtlasDao.class);

	this.softSchools = context.getInstance(SoftSchools.class);
	this.freeDictionary = context.getInstance(TheFreeDictionary.class);
	this.cambridgeDictionary = context.getInstance(CambridgeDictionary.class);
	this.merriamWebster = context.getInstance(MerriamWebster.class);

	this.animalia = context.getInstance(Animalia.class);
	this.wikipedia = context.getInstance(Wikipedia.class);
    }

    @Override
    public List<ApiDescriptor> getAvailableApis() {
	return availableApis;
    }

    @Override
    public List<ApiDescriptor> getApiDescriptors(List<String> apiNames) {
	List<ApiDescriptor> apis = new ArrayList<>();
	for (ApiDescriptor api : availableApis) {
	    if (apiNames.contains(api.getName())) {
		apis.add(api);
	    }
	}
	return apis;
    }

    @Override
    public Object invokeAPI(String apiName, Link link) {
	Method method = apiMethods.get(apiName);
	if (method == null) {
	    throw new BugError("Not registered import API |%s|.", apiName);
	}
	try {
	    return method.invoke(this, link);
	} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	    log.error(e);
	    throw new RuntimeException(String.format("Error executing import API |%s|.", apiName));
	}
    }

    @Override
    @API(name = "definition", description = "Definition is a brief description, usually one statement.")
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
    @API(name = "description", description = "Description is organized on named sections with statements related by meaning.")
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
    @API(name = "facts", description = "A fact is a paragraph describing a piece of reality and has only one independent statement.")
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
    @API(name = "taxonomy", description = "Object classification. For now only life forms taxonomy is supported.")
    public List<Taxon> getTaxonomy(Link link) {
	LinkedHashMap<String, String> taxonomy = null;
	switch (link.getDomain()) {
	case "wikipedia.org":
	    taxonomy = wikipedia.getLifeTaxonomy(link.getBasename());
	    break;

	default:
	    return null;
	}

	AtlasCollection collection = atlasDao.getCollectionByLinkSource(link.getLinkSource().getId());
	return loadTaxonomy(collection.getTaxonomyMeta(), taxonomy);
    }

    @Override
    @API(name = "physical-traits", description = "Common physical traits for a specific animal species.")
    public List<Feature> getPhysicalTraits(Link link) {
	AtlasCollection collection = atlasDao.getCollectionByLinkSource(link.getLinkSource().getId());
	List<Feature> features = new ArrayList<>();

	List<PhysicalTrait> traits = null;
	switch (link.getDomain()) {
	case "animalia.bio":
	    traits = animalia.getPhysicalTraits(link.getBasename());
	    break;
	}

	if (traits != null) {
	    for (FeatureMeta meta : collection.getFeaturesMeta()) {
		for (PhysicalTrait trait : traits) {
		    if (meta.getName().equals(trait.getName())) {
			Feature feature = new Feature(meta, trait.getValue(), trait.getMaximum());
			feature.postLoad();
			features.add(feature);
			break;
		    }
		}
	    }
	}
	return features;
    }

    @Override
    @API(name = "edible-nutrients", description = "Nutritional values for 100 grams of an edible plant or animal.")
    public List<Feature> getEdibleNutrients(Link link) {
	AtlasCollection collection = atlasDao.getCollectionByLinkSource(link.getLinkSource().getId());
	List<Feature> features = new ArrayList<>();

	Map<String, Double> nutrients = null;
	switch (link.getDomain()) {
	case "wikipedia.org":
	    nutrients = wikipedia.getEdibleNutrients(link.getBasename());
	    break;
	}

	if (nutrients != null) {
	    for (FeatureMeta meta : collection.getFeaturesMeta()) {
		for (String label : nutrients.keySet()) {
		    // all feature meta names related to nutrients have at least one dot
		    // if not used dot on name scanning there is confusion between 'saturated' and
		    // 'monounsaturated' because both ends with 'saturated'
		    if (meta.getName().endsWith("." + Strings.toDotCase(label))) {
			Feature feature = new Feature(meta, nutrients.get(label));
			feature.postLoad();
			features.add(feature);
			break;
		    }
		}
	    }
	}

	return features;
    }

    @Override
    @API(name = "metro-statistics", description = "Statistical values regarding metropolitan area, like population size and density.")
    public List<Feature> getMetroStatistics(Link link) {
	AtlasCollection collection = atlasDao.getCollectionByLinkSource(link.getLinkSource().getId());
	List<Feature> features = new ArrayList<>();

	Map<String, Double> statistics = null;
	switch (link.getDomain()) {
	case "wikipedia.org":
	    // statistics = wikipedia.getMetroStatistics(link.getBasename());
	    statistics = Collections.emptyMap();
	    break;
	}

	if (statistics != null) {
	    for (FeatureMeta meta : collection.getFeaturesMeta()) {
		for (String label : statistics.keySet()) {
		    if (meta.getName().equals(label)) {
			Feature feature = new Feature(meta, statistics.get(label));
			feature.postLoad();
			features.add(feature);
			break;
		    }
		}
	    }
	}

	return features;
    }

    @Override
    @API(name = "military-specifications", description = "Technical specifications for military technology.")
    public List<Feature> getMilitarySpecifications(Link link) {
	AtlasCollection collection = atlasDao.getCollectionByLinkSource(link.getLinkSource().getId());
	List<Feature> features = new ArrayList<>();

	Map<String, Double> statistics = null;
	switch (link.getDomain()) {
	case "wikipedia.org":
	    // statistics = wikipedia.getMilitarySpecifications(link.getBasename());
	    statistics = Collections.emptyMap();
	    break;
	}

	if (statistics != null) {
	    for (FeatureMeta meta : collection.getFeaturesMeta()) {
		for (String label : statistics.keySet()) {
		    if (meta.getName().equals(label)) {
			Feature feature = new Feature(meta, statistics.get(label));
			feature.postLoad();
			features.add(feature);
			break;
		    }
		}
	    }
	}

	return features;
    }

    @Override
    public AtlasItem importWikipediaObject(int collectionId, URL articleURL) {
	LifeForm lifeForm = wikipedia.getLifeForm(Files.basename(articleURL.getPath()));
	log.debug("Import life form |%s|.", lifeForm.getCommonName());

	String domain = Strings.basedomain(articleURL);
	AtlasCollection collection = atlasDao.getCollectionById(collectionId);
	AtlasObject object = AtlasObject.create(collection);

	object.setName(Strings.scientificToDashedName(lifeForm.getScientificName()));
	if (object.getName() == null) {
	    object.setName(lifeForm.getCommonName().trim().toLowerCase().replaceAll("[()]", "").replaceAll(" ", "-"));
	}
	object.setDisplay(lifeForm.getCommonName());
	object.setDefinition(lifeForm.getDefinition());

	StringBuilder builder = new StringBuilder();
	builder.append("<text>");
	for (DescriptionMeta descriptionMeta : collection.getDescriptionMeta()) {
	    builder.append("<section name=\"");
	    builder.append(descriptionMeta.getName());
	    builder.append("\"></section>");
	}
	builder.append("<section name=\"wikipedia\">");
	for (String line : lifeForm.getDescription()) {
	    builder.append("<p>");
	    builder.append(line);
	    builder.append("</p>");
	}
	builder.append("</section></text>");
	object.setDescription(builder.toString());

	if (lifeForm.getStartDate() != null) {
	    object.setStartDate(new HDate(lifeForm.getStartDate()));
	}
	if (lifeForm.getEndDate() != null && collection.getFlags().hasEndDate()) {
	    object.setEndDate(new HDate(lifeForm.getEndDate()));
	}
	if (collection.getFlags().hasConservationStatus()) {
	    object.setConservation(ConservationStatus.forDisplay(lifeForm.getConservationStatus()));
	}

	object.setTaxonomy(loadTaxonomy(collection.getTaxonomyMeta(), lifeForm.getTaxonomy()));

	ExternalSource externalSource = atlasDao.getExternalSourceByDomain(domain);
	LinkSource linkSource = atlasDao.getLinkSourceByDomain(collectionId, domain);

	List<Link> links = new ArrayList<>();
	links.add(
		Link.create(linkSource, articleURL, externalSource.getLinkDefinition(articleURL, object.getDisplay())));
	object.setLinks(links);

	object.setState(AtlasObject.State.CREATED);
	object.setAliases(new ArrayList<String>());
	object.setImages(new HashMap<String, Image>());
	object.updateTimestamp();

	atlasDao.saveAtlasObject(object);
	return atlasDao.getAtlasItem(object.getId());
    }

    private static List<Taxon> loadTaxonomy(List<TaxonMeta> taxonomyMeta, Map<String, String> taxonomyValues) {
	if (taxonomyValues.isEmpty()) {
	    return null;
	}
	List<Taxon> taxonomy = new ArrayList<>();
	for (TaxonMeta taxonMeta : taxonomyMeta) {
	    for (Map.Entry<String, String> taxon : taxonomyValues.entrySet()) {
		if (!taxonMeta.getName().equals(taxon.getKey())) {
		    continue;
		}
		if (taxonMeta.getValues() != null) {
		    if (!Strings.split(taxonMeta.getValues(), ',').contains(taxon.getValue())) {
			continue;
		    }
		}
		taxonomy.add(new Taxon(taxonMeta, taxon.getValue()));
		break;
	    }
	}
	return taxonomy;
    }
}
