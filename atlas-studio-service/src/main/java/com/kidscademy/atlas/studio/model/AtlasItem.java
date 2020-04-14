package com.kidscademy.atlas.studio.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.util.Files;

/**
 * Lightweight {@link AtlasObject} usually used as item in a collection. It has
 * only enough fields to be displayed as an icon with a name.
 * 
 * @author Iulian Rotaru
 */
@Entity
public class AtlasItem implements RepositoryObject, GraphicObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Transient
    private final String title = "Atlas Object";

    @ManyToOne
    private AtlasCollection collection;
    
    @Enumerated(EnumType.STRING)
    private AtlasObject.State state;
    
    private Date timestamp;
    private String name;
    private String display;
    private String definition;

    /**
     * Media file name for object icon. Object icon has a small dimension and has
     * 1:1 ratio; usually is 96x96 pixels. This field is optional and can be null.
     */
    private String iconName;

    @Transient
    private MediaSRC iconSrc;

    public AtlasItem() {
    }

    /**
     * Test constructor.
     * 
     * @param collection
     * @param id
     * @param name
     */
    public AtlasItem(AtlasCollection collection, int id, String name) {
	this.collection = collection;
	this.id = id;
	this.name = name;
    }

    @PostLoad
    public void postLoad() {
	if (iconName != null) {
	    iconSrc = Files.mediaSrc(this, iconName, "96x96");
	}
    }

    public int getId() {
	return id;
    }

    public void setCollection(AtlasCollection collection) {
        this.collection = collection;
    }

    public AtlasCollection getCollection() {
	return collection;
    }

    @Override
    public String getRepositoryName() {
	return collection.getName();
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

    public AtlasObject.State getState() {
	return state;
    }

    public String getIconName() {
	return iconName;
    }

    @Override
    public MediaSRC getIconSrc() {
	return iconSrc;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + id;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	AtlasItem other = (AtlasItem) obj;
	if (id != other.id)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return display;
    }
}
