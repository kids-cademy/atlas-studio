package com.kidscademy.atlas.studio.export;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kidscademy.atlas.studio.model.AtlasItem;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.ConservationStatus;
import com.kidscademy.atlas.studio.model.Fact;
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
public class ExportObject
{
  // source atlas object is needed for export processing but is not serialized to target JSON
  private transient AtlasObject atlasObject;

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

  public ExportObject(AtlasObject atlasObject) {
    this(atlasObject, atlasObject.getCollection().getTheme());
  }

  public ExportObject(AtlasObject atlasObject, Theme theme) {
    this.atlasObject = atlasObject;
    this.name = atlasObject.getName();

    this.language = Translator.DEFAULT_LANGUAGE;
    this.display = atlasObject.getDisplay();
    this.aliases = atlasObject.getAliases();
    this.definition = atlasObject.getDefinition();
    this.description = exportDescription(atlasObject.getDescription());
    this.sampleTitle = atlasObject.getSampleTitle();

    this.taxonomy = new ArrayList<>();
    for(Taxon taxon : atlasObject.getTaxonomy()) {
      this.taxonomy.add(new ExportTaxon(taxon.getDisplay(), taxon.getValue()));
    }

    this.images = new HashMap<>();
    for(Map.Entry<String, Image> entry : atlasObject.getImages().entrySet()) {
      this.images.put(entry.getKey(), new ExportImage(atlasObject, entry.getValue()));
    }

    this.lastUpdated = atlasObject.getTimestamp();

    this.spreading = atlasObject.getSpreading();
    this.startDate = atlasObject.getStartDate();
    this.endDate = atlasObject.getEndDate();
    this.progenitor = atlasObject.getProgenitor();
    this.conservation = atlasObject.getConservation();

    this.samplePath = Util.path(atlasObject.getName(), atlasObject.getSampleName());
    this.waveformPath = Util.path(atlasObject.getName(), atlasObject.getWaveformName());
    this.waveformSrc = atlasObject.getWaveformSrc() != null ? atlasObject.getWaveformSrc().value() : null;

    this.features = new ArrayList<>();
    for(Feature feature : atlasObject.getFeatures()) {
      this.features.add(new ExportFeature(feature));
    }

    this.facts = new ArrayList<>();
    for(Fact fact : atlasObject.getFacts()) {
      this.facts.add(new ExportFact(fact.getTitle(), fact.getTitle()));
    }

    this.related = new ArrayList<>();

    this.links = new ArrayList<>();
    for(Link link : atlasObject.getLinks()) {
      this.links.add(new ExportLink(link));
    }

    this.theme = theme.cssStyle();
  }

  public void translate(Translator translator) {
    language = translator.getLanguage();
    display = translator.getAtlasObjectDisplay(atlasObject.getId());
    aliases = translator.getAtlasObjectAliases(atlasObject.getId());
    definition = translator.getAtlasObjectDefinition(atlasObject.getId());
    description = exportDescription(translator.getAtlasObjectDescription(atlasObject.getId()));
    sampleTitle = translator.getAtlasObjectSampleTitle(atlasObject.getId());

    taxonomy.clear();
    for(Taxon taxon : atlasObject.getTaxonomy()) {
      taxonomy.add(new ExportTaxon(translator.getTaxonMetaDisplay(taxon.getMeta().getId()), translator.getTaxonValue(taxon.getId())));
    }

    features.clear();
    for(Feature feature : atlasObject.getFeatures()) {
      this.features.add(new ExportFeature(feature));
    }

    facts.clear();
    for(Fact fact : atlasObject.getFacts()) {
      facts.add(new ExportFact(translator.getFactTitle(fact.getId()), translator.getFactText(fact.getId())));
    }
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
    if(description == null) {
      return null;
    }

    DocumentBuilder builder = Classes.loadService(DocumentBuilder.class);
    Document document = builder.parseXML(description);

    List<String> paragraphs = new ArrayList<String>();
    for(Element paragraph : document.findByTag("p")) {
      paragraphs.add(paragraph.getText());
    }

    return paragraphs;
  }
}
