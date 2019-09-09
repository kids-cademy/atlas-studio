package com.kidscademy.atlas.studio.tool;

import java.util.Date;
import java.util.List;
import java.util.Map;

class AtlasObject {
    private int id;
    private int user_id;
    private int colleciton_id;
    
    State state;
    Date lastUpdated;

    int rank;
    String name;
    String display;
    String definition;
    String description;

    List<Image> images;

    Map<String, String> classification;
    List<String> aliases;
    List<Region> spreading;
    HDate startDate;
    HDate endDate;

    String sampleTitle;
    String sampleName;
    String waveformName;

    Map<String, String> facts;
    Map<String, String> features;
    List<String> related;
    List<Link> links;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getColleciton_id() {
        return colleciton_id;
    }

    public void setColleciton_id(int colleciton_id) {
        this.colleciton_id = colleciton_id;
    }

    public void setState(State state) {
	this.state = state;
    }

    public void setLastUpdated(Date lastUpdated) {
	this.lastUpdated = lastUpdated;
    }

    public void setRank(int rank) {
	this.rank = rank;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setDisplay(String display) {
	this.display = display;
    }

    public void setDefinition(String definition) {
	this.definition = definition;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public void setImages(List<Image> images) {
	this.images = images;
    }

    public void setClassification(Map<String, String> classification) {
	this.classification = classification;
    }

    public void setAliases(List<String> aliases) {
	this.aliases = aliases;
    }

    public void setSpreading(List<Region> spreading) {
	this.spreading = spreading;
    }

    public void setStartDate(HDate startDate) {
	this.startDate = startDate;
    }

    public void setEndDate(HDate endDate) {
	this.endDate = endDate;
    }

    public void setSampleTitle(String sampleTitle) {
	this.sampleTitle = sampleTitle;
    }

    public void setSampleName(String sampleName) {
	this.sampleName = sampleName;
    }

    public void setWaveformName(String waveformName) {
	this.waveformName = waveformName;
    }

    public void setFacts(Map<String, String> facts) {
	this.facts = facts;
    }

    public void setFeatures(Map<String, String> features) {
	this.features = features;
    }

    public void setRelated(List<String> related) {
	this.related = related;
    }

    public void setLinks(List<Link> links) {
	this.links = links;
    }

    public State getState() {
	return state;
    }

    public Date getLastUpdated() {
	return lastUpdated;
    }

    public int getRank() {
	return rank;
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

    public List<Image> getImages() {
	return images;
    }

    public Map<String, String> getClassification() {
	return classification;
    }

    public List<String> getAliases() {
	return aliases;
    }

    public List<Region> getSpreading() {
	return spreading;
    }

    public HDate getStartDate() {
	return startDate;
    }

    public HDate getEndDate() {
	return endDate;
    }

    public String getSampleTitle() {
	return sampleTitle;
    }

    public String getSampleName() {
	return sampleName;
    }

    public String getWaveformName() {
	return waveformName;
    }

    public Map<String, String> getFacts() {
	return facts;
    }

    public Map<String, String> getFeatures() {
	return features;
    }

    public List<String> getRelated() {
	return related;
    }

    public List<Link> getLinks() {
	return links;
    }

    public enum State {
	// ENUM('DEVELOPMENT','PUBLISHED')
	DEVELOPMENT, PUBLISHED
    }
}
