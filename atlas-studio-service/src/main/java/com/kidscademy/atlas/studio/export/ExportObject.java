package com.kidscademy.atlas.studio.export;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.ConservationStatus;
import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.HDate;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.Region;
import com.kidscademy.atlas.studio.model.Taxon;
import com.kidscademy.atlas.studio.model.Theme;
import com.kidscademy.atlas.studio.model.Translator;

import js.dom.Document;
import js.dom.DocumentBuilder;
import js.dom.Element;
import js.util.Classes;

@SuppressWarnings("unused")
public class ExportObject {
    private int index;

    private String name;
    private String language;
    private String display;
    private List<String> aliases;
    private String definition;
    private List<String> description;

    private Map<String, ExportImage> images;

    private Date lastUpdated;
    private List<ExportTaxon> taxonomy;
    private List<Region> spreading;
    private HDate startDate;
    private HDate endDate;
    private String progenitor;
    private ConservationStatus conservation;

    private String sampleTitle;
    private String samplePath;
    private String waveformPath;
    private String waveformSrc;

    private List<ExportFact> facts;
    private List<ExportFeature> features;
    private List<ExportRelatedObject> related;
    private List<ExportLink> links;

    private String theme;

    public ExportObject() {
    }

    public ExportObject(AtlasObject object) {
	this(object, Translator.getDefaultInstance(), object.getCollection().getTheme());
    }

    public ExportObject(AtlasObject object, Translator translator) {
	this(object, translator, object.getCollection().getTheme());
    }

    public ExportObject(AtlasObject object, Translator translator, Theme theme) {
	this.name = object.getName();

	if (translator.isDefaultLanguage()) {
	    this.language = "EN";
	    this.display = object.getDisplay();
	    this.aliases = object.getAliases();
	    this.definition = object.getDefinition();
	    this.description = exportDescription(object.getDescription());
	} else {
	    this.language = translator.getLanguage();
	    this.display = translator.getAtlasObjectDisplay(object.getId());
	    this.aliases = translator.getAtlasObjectAliases(object.getId());
	    this.definition = translator.getAtlasObjectDefinition(object.getId());
	    this.description = exportDescription(translator.getAtlasObjectDescription(object.getId()));
	}

	this.images = new HashMap<>();
	for (Map.Entry<String, Image> entry : object.getImages().entrySet()) {
	    this.images.put(entry.getKey(), new ExportImage(object, entry.getValue()));
	}

	this.lastUpdated = object.getTimestamp();
	this.taxonomy = new ArrayList<>();
	for (Taxon taxon : object.getTaxonomy()) {
	    this.taxonomy.add(new ExportTaxon(taxon));
	}

	this.spreading = object.getSpreading();
	this.startDate = object.getStartDate();
	this.endDate = object.getEndDate();
	this.progenitor = object.getProgenitor();
	this.conservation = object.getConservation();

	this.sampleTitle = object.getSampleTitle();
	this.samplePath = Util.path(object.getName(), object.getSampleName());
	this.waveformPath = Util.path(object.getName(), object.getWaveformName());
	this.waveformSrc = object.getWaveformSrc() != null ? object.getWaveformSrc().value() : null;

	this.facts = new ArrayList<>();
	for (Map.Entry<String, String> entry : object.getFacts().entrySet()) {
	    this.facts.add(new ExportFact(entry));
	}

	this.features = new ArrayList<>();
	for (Feature feature : object.getFeatures()) {
	    this.features.add(new ExportFeature(feature));
	}

	this.related = new ArrayList<>();

	this.links = new ArrayList<>();
	for (Link link : object.getLinks()) {
	    this.links.add(new ExportLink(link));
	}

	this.theme = theme.cssStyle();
    }

    public String getLanguage() {
	return language;
    }

    public void setIndex(int index) {
	this.index = index;
    }

    public int getIndex() {
	return index;
    }

    public void addRelated(ExportRelatedObject relatedObject) {
	related.add(relatedObject);
    }

    public void addRelated(AtlasItem item) {
	related.add(new ExportRelatedObject(item));
    }

    public String getName() {
	return name;
    }

    public String getDisplay() {
	return display;
    }

    public String getDefinition() {
	return definition;
    }

    public List<String> getDescription() {
	return description;
    }

    public List<ExportTaxon> getTaxonomy() {
	return taxonomy;
    }

    public List<String> getAliases() {
	return aliases;
    }

    public List<Region> getSpreading() {
	return spreading;
    }

    public ConservationStatus getConservation() {
	return conservation;
    }

    public String getSampleTitle() {
	return sampleTitle;
    }

    public List<ExportRelatedObject> getRelated() {
	return related;
    }

    public List<ExportFact> getFacts() {
	return facts;
    }

    public List<ExportFeature> getFeatures() {
	return features;
    }

    public ExportImage getImage(String imageKey) {
	return images.get(imageKey);
    }

    private static List<String> exportDescription(String description) {
	if (description == null) {
	    return null;
	}

	DocumentBuilder builder = Classes.loadService(DocumentBuilder.class);
	Document document = builder.parseXML(description);

	List<String> paragraphs = new ArrayList<String>();
	for (Element paragraph : document.findByTag("p")) {
	    paragraphs.add(paragraph.getText());
	}

	return paragraphs;
    }
}
