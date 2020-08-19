package com.kidscademy.atlas.studio.model;

import java.util.ArrayList;
import java.util.List;

public class AtlasObjectTranslate
{
  private String display;
  private List<String> aliases;
  private String definition;
  private String description;
  
  private String sampleTitle;
  private String featuredCaption;
  private String triviaCaption;
  private String contextualCaption;
  
  private List<Fact> facts;

  public AtlasObjectTranslate() {

  }

  public AtlasObjectTranslate(AtlasObject object, Translator translator) {
    this.display = translator.getAtlasObjectDisplay(object.getId());
    this.aliases = translator.getAtlasObjectAliases(object.getId());
    this.definition = translator.getAtlasObjectDefinition(object.getId());
    this.description = translator.getAtlasObjectDescription(object.getId());
    
    this.sampleTitle = translator.getAtlasObjectSampleTitle(object.getId());

    this.facts = new ArrayList<>();
    for(Fact fact : object.getFacts()) {
      final int id = fact.getId();
      this.facts.add(new Fact(id, translator.getFactTitle(id), translator.getFactText(id)));
    }
  }

  public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public List<String> getAliases() {
    return aliases;
  }

  public void setAliases(List<String> aliases) {
    this.aliases = aliases;
  }

  public String getDefinition() {
    return definition;
  }

  public void setDefinition(String definition) {
    this.definition = definition;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getSampleTitle() {
    return sampleTitle;
  }

  public void setSampleTitle(String sampleTitle) {
    this.sampleTitle = sampleTitle;
  }

  public String getFeaturedCaption() {
    return featuredCaption;
  }

  public void setFeaturedCaption(String featuredCaption) {
    this.featuredCaption = featuredCaption;
  }

  public String getTriviaCaption() {
    return triviaCaption;
  }

  public void setTriviaCaption(String triviaCaption) {
    this.triviaCaption = triviaCaption;
  }

  public String getContextualCaption() {
    return contextualCaption;
  }

  public void setContextualCaption(String contextualCaption) {
    this.contextualCaption = contextualCaption;
  }

  public List<Fact> getFacts() {
    return facts;
  }

  public void setFacts(List<Fact> facts) {
    this.facts = facts;
  }
}
