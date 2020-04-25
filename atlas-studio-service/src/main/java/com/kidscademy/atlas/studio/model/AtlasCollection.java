package com.kidscademy.atlas.studio.model;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OrderColumn;
import javax.persistence.PostLoad;
import javax.persistence.PostRemove;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.util.Files;

import js.lang.BugError;
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
public class AtlasCollection implements GraphicObject {
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

    @ManyToMany
    @OrderColumn
    private List<FeatureMeta> featuresMeta;

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
	this.name = name;
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

	// remove collection objects media repository, that is, directory 
	File repositoryDir = Files.repositoryDir(name);
	if (repositoryDir.isDirectory()) {
	    String[] files = repositoryDir.list();
	    if (files != null && files.length > 0) {
		throw new BugError("Empty collection |%s| should have empty media directory.", name);
	    }
	    if (!repositoryDir.delete()) {
		log.error("Cannot remove media directory for collection |%s|.", name);
	    }
	}
    }

    public int getId() {
	return id;
    }

    public boolean isPersisted() {
	return id != 0;
    }

    @Override
    public Date getTimestamp() {
	return timestamp;
    }

    @Override
    public String getTitle() {
	return title;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public String getDisplay() {
	return display;
    }

    @Override
    public String getDefinition() {
	return definition;
    }

    @Override
    public MediaSRC getIconSrc() {
	return iconSrc;
    }

    public List<TaxonMeta> getTaxonomyMeta() {
	return taxonomyMeta;
    }

    public Flags getFlags() {
	return flags;
    }

    public List<FeatureMeta> getFeaturesMeta() {
	return featuresMeta;
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
