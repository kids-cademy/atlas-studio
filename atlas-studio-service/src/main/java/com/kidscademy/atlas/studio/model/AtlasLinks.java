package com.kidscademy.atlas.studio.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.util.Files;

@Entity
@Table(name = "atlasitem")
public class AtlasLinks implements RepositoryObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private AtlasCollectionKey collection;

    @Enumerated(EnumType.STRING)
    private AtlasObject.State state;

    private String name;
    private String display;
    private String iconName;
    private Date timestamp;

    @ElementCollection
    @OrderColumn
    @CollectionTable(name = "atlasobject_links", joinColumns = @JoinColumn(name = "atlasobject_id"))
    private List<Link> links;

    @Transient
    private MediaSRC iconSrc;

    @Transient
    private List<MediaSRC> items;

    @PostLoad
    public void postLoad() {
	if (iconName != null) {
	    iconSrc = Files.mediaSrc(this, iconName);
	}
	items = new ArrayList<>(links.size());
	for (Link link : links) {
	    link.postLoad();
	    items.add(link.getIconSrc());
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

    public AtlasCollectionKey getCollection() {
	return collection;
    }

    public String getDisplay() {
	return display;
    }

    public AtlasObject.State getState() {
	return state;
    }

    public Date getTimestamp() {
	return timestamp;
    }
}
