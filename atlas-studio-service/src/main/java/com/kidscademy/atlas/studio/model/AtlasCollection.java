package com.kidscademy.atlas.studio.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Atlas collection groups together {@link AtlasObject} that have something in
 * common. It is the primary mean to distribute atlas content. An atlas
 * application release contains a collection.
 * 
 * @author Iulian Rotaru
 */
@Entity
@Cacheable
public class AtlasCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String display;

    public AtlasCollection() {
    }

    /**
     * Test constructor.
     * 
     * @param name
     */
    public AtlasCollection(String name) {
	this.name = name;
    }

    public int getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public String getDisplay() {
	return display;
    }
}
