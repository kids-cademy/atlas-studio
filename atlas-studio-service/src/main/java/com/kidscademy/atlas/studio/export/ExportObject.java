package com.kidscademy.atlas.studio.export;

import java.util.ArrayList;
import java.util.Arrays;
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
    private final String progenitor;
    private final ConservationStatus conservation;

    private final String sampleTitle;
    private final String samplePath;
    private final String waveformPath;
    private final String waveformSrc;

    private final List<ExportFact> facts;
    private final List<ExportFeature> features;
    private final List<ExportRelatedObject> related;
    private final List<ExportLink> links;

    private final String theme;
    
    public ExportObject(AtlasObject object) {
	this.name = object.getName();
	this.display = object.getDisplay();
	this.definition = object.getDefinition();
	this.description = exportDescription(object.getDescription());

	this.images = new HashMap<>();
	for (Map.Entry<String, Image> entry : object.getImages().entrySet()) {
	    this.images.put(entry.getKey(), new ExportImage(object, entry.getValue()));
	}

	this.lastUpdated = object.getTimestamp();
	this.taxonomy = new ArrayList<>();
	for (Taxon taxon : object.getTaxonomy()) {
	    this.taxonomy.add(new ExportTaxon(taxon));
	}

	this.aliases = object.getAliases();
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
	
	this.theme = object.getCollection().getTheme().cssStyle();
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

    private static String exportDescription(String description) {
	if (description == null) {
	    return null;
	}
	StringBuilder exportBuilder = new StringBuilder();
	StringBuilder tagBuilder = new StringBuilder();

	DescriptionState state = DescriptionState.LINE;
	boolean endTag = false;
	for (int i = 0; i < description.length(); ++i) {
	    char c = description.charAt(i);

	    switch (state) {
	    case TAG:
		if (c == '/') {
		    endTag = true;
		    break;
		}
		if (c == '>') {
		    if (!excludes(tagBuilder.toString())) {
			exportBuilder.append('<');
			if (endTag) {
			    exportBuilder.append('/');
			}
			exportBuilder.append(tagBuilder);
			exportBuilder.append('>');
		    }
		    endTag = false;
		    tagBuilder.setLength(0);
		    state = DescriptionState.LINE;
		    break;
		}
		tagBuilder.append(c);
		break;

	    case LINE:
		if (c == '<') {
		    state = DescriptionState.TAG;
		    break;
		}
		exportBuilder.append(c);
		break;
	    }
	}

	return exportBuilder.toString();
    }

    private static List<String> DESCRIPTION_EXCLUDE_TAGS = Arrays.asList("text", "section", "em", "strong");

    private static boolean excludes(String tag) {
	int tagNameEndPosition = tag.indexOf(' ');
	if (tagNameEndPosition == -1) {
	    tagNameEndPosition = tag.length();
	}
	return DESCRIPTION_EXCLUDE_TAGS.contains(tag.substring(0, tagNameEndPosition));
    }

    private enum DescriptionState {
	TAG, LINE
    }
}
