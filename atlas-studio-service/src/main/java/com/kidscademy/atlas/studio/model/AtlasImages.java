package com.kidscademy.atlas.studio.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "atlasitem")
public class AtlasImages implements RepositoryObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private AtlasCollection collection;

    private String name;
    private String display;
    private AtlasObject.State state;
    private Date lastUpdated;

    @ElementCollection
    @MapKeyColumn(name = "imageKey")
    @CollectionTable(name = "atlasobject_images", joinColumns = @JoinColumn(name = "atlasobject_id"))
    private Map<String, Image> images;

    @Transient
    private List<MediaSRC> items;
    
    @PostLoad
    public void postLoad() {
	for (Image picture : images.values()) {
	    picture.postLoad(this);
	}
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public String getRepositoryName() {
	return collection.getName();
    }

    public int getId() {
	return id;
    }

    public AtlasCollection getCollection() {
	return collection;
    }

    public String getDisplay() {
	return display;
    }

    public AtlasObject.State getState() {
        return state;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public Map<String, Image> getImages() {
	return images;
    }
}
