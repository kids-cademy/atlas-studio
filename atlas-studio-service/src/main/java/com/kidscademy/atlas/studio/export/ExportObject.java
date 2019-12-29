package com.kidscademy.atlas.studio.export;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.ConservationStatus;
import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.HDate;
import com.kidscademy.atlas.studio.model.Image;
import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.model.Region;
import com.kidscademy.atlas.studio.model.Taxon;

@SuppressWarnings("unused")
public class ExportObject {
    private int index;

    private final int rank;
    private final String name;
    private final String display;
    private final String definition;
    private final String description;

    private final Map<String, ExportImage> images;

    private final Date lastUpdated;
    private final List<ExportTaxon> taxonomy;
    private final List<String> aliases;
    private final List<Region> spreading;
    private final HDate startDate;
    private final HDate endDate;
    private final ConservationStatus conservation;

    private final String sampleTitle;
    private final String samplePath;
    private final String waveformPath;

    private final List<ExportFact> facts;
    private final List<ExportFeature> features;
    private final List<ExportRelatedObject> related;
    private final List<ExportLink> links;

    public ExportObject(AtlasObject object) {
	this.rank = object.getRank();
	this.name = object.getName();
	this.display = object.getDisplay();
	this.definition = object.getDefinition();
	this.description = object.getDescription();

	this.images = new HashMap<>();
	for (Map.Entry<String, Image> entry : object.getImages().entrySet()) {
	    this.images.put(entry.getKey(), new ExportImage(object, entry.getValue()));
	}

	this.lastUpdated = object.getLastUpdated();
	this.taxonomy = new ArrayList<>();
	for (Taxon taxon : object.getTaxonomy()) {
	    this.taxonomy.add(new ExportTaxon(taxon));
	}

	this.aliases = object.getAliases();
	this.spreading = object.getSpreading();
	this.startDate = object.getStartDate();
	this.endDate = object.getEndDate();
	this.conservation = object.getConservation();

	this.sampleTitle = object.getSampleTitle();
	this.samplePath = Util.path(object.getName(), object.getSampleName());
	this.waveformPath = Util.path(object.getName(), object.getWaveformName());

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

    public String getDescription() {
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
}
