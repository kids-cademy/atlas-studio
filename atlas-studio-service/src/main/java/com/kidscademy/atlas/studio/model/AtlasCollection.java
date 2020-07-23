package com.kidscademy.atlas.studio.model;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PostLoad;
import javax.persistence.PostRemove;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.util.Files;

import js.log.Log;
import js.log.LogFactory;

/**
 * Atlas collection groups together {@link AtlasObject} that have something in
 * common. It is the primary mean to distribute atlas content. An atlas
 * application release contains a collection.
 * 
 * @author Iulian Rotaru
 */
@Entity
public class AtlasCollection implements Key, GraphicObject {
    private static final Log log = LogFactory.getLog(AtlasCollection.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Transient
    private final String title = "Atlas Collection";

    private Date timestamp;
    private String name;
    private String display;
    private String definition;

    @ElementCollection
    @OrderColumn
    private List<TaxonMeta> taxonomyMeta;

    @ElementCollection
    @OrderColumn
    private List<DescriptionMeta> descriptionMeta;

    @ManyToMany
    @OrderColumn
    private List<FeatureMeta> featuresMeta;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "atlascollection_id")
    @OrderColumn
    private List<LinkSource> linkSources;

    private String theme;
    
    private Flags flags;

    @Transient
    private MediaSRC iconSrc;

    public AtlasCollection() {
    }

    /**
     * Test constructor.
     * 
     * @param id
     * @param name
     */
    public AtlasCollection(int id, String name) {
	this.id = id;
	this.timestamp = new Date();
	this.name = name;
	this.taxonomyMeta = Collections.emptyList();
	this.descriptionMeta = Collections.emptyList();
	this.featuresMeta = Collections.emptyList();
	this.linkSources = Collections.emptyList();
	this.theme = "classic";
	this.flags = new Flags(true);
    }

    @PostLoad
    public void postLoad() {
	iconSrc = Files.mediaSrc(this);
    }

    @PostRemove
    public void postRemove() {
	// remove collection icon and scaled variant
	File icon = Files.mediaFile(this);
	if (!icon.delete()) {
	    log.error("Cannot remove collection icon |%s|.", icon);
	}
	icon = Files.mediaFile(this, "96x96");
	if (!icon.delete()) {
	    log.error("Cannot remove collection icon |%s|.", icon);
	}

	// remove collection objects media repository
	File repositoryDir = Files.repositoryDir(name);
	if (repositoryDir.isDirectory()) {
	    String[] files = repositoryDir.list();
	    if (files != null && files.length > 0) {
		log.error("Empty collection |%s| should have empty media directory.", name);
		return;
	    }
	    if (!repositoryDir.delete()) {
		log.error("Cannot remove media directory for collection |%s|.", name);
	    }
	}
    }

    @Override
    public int getId() {
	return id;
    }

    public boolean isPersisted() {
	return id != 0;
    }

    public Date getTimestamp() {
	return timestamp;
    }

    @Override
    public String getTitle() {
	return title;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String getName() {
	return name;
    }

    public void setDisplay(String display) {
	this.display = display;
    }

    @Override
    public String getDisplay() {
	return display;
    }

    public void setDefinition(String definition) {
	this.definition = definition;
    }

    @Override
    public String getDefinition() {
	return definition;
    }

    @Override
    public MediaSRC getIconSrc() {
	return iconSrc;
    }

    public void setDescriptionMeta(List<DescriptionMeta> descriptionMeta) {
	this.descriptionMeta = descriptionMeta;
    }

    public List<DescriptionMeta> getDescriptionMeta() {
	return descriptionMeta;
    }

    public void setTaxonomyMeta(List<TaxonMeta> taxonomyMeta) {
	this.taxonomyMeta = taxonomyMeta;
    }

    public List<TaxonMeta> getTaxonomyMeta() {
	return taxonomyMeta;
    }

    public void setFeaturesMeta(List<FeatureMeta> featuresMeta) {
	this.featuresMeta = featuresMeta;
    }

    public boolean hasFeaturesMeta() {
	return featuresMeta != null && !featuresMeta.isEmpty();
    }

    public List<FeatureMeta> getFeaturesMeta() {
	return featuresMeta;
    }

    public void setLinkSources(List<LinkSource> linkSources) {
	this.linkSources = linkSources;
    }

    public List<LinkSource> getLinkSources() {
	return linkSources;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setFlags(Flags flags) {
	this.flags = flags;
    }

    public Flags getFlags() {
	return flags;
    }

    public static AtlasCollection create() {
	AtlasCollection collection = new AtlasCollection();
	collection.flags = new Flags();
	collection.flags.setEndDate(true);
	collection.flags.setProgenitor(true);
	collection.flags.setConservationStatus(true);
	collection.flags.setAudioSample(true);
	collection.flags.setSpreading(true);
	return collection;
    }
}
