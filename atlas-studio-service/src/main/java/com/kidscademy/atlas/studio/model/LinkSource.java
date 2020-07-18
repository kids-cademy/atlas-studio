package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.util.Files;

import js.tiny.container.annotation.TestConstructor;

/**
 * Link source is and external source with a subset of APIs. Although is an
 * entity, a LinkSource life cycle is controlled by AtlasCollection.
 * <p>
 * LinkSource purpose is to control the set of APIs that atlas collection has.
 * 
 * @author Iulian Rotaru
 */
@Entity
public class LinkSource implements Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private ExternalSource externalSource;

    private String apis;

    @Transient
    private String display;

    @Transient
    private MediaSRC iconSrc;

    public LinkSource() {

    }

    public LinkSource(ExternalSource externalSource) {
	this.externalSource = externalSource;
	this.apis = externalSource.getApis();
    }

    @TestConstructor
    public LinkSource(int id, ExternalSource externalSource) {
	this.id = id;
	this.externalSource = externalSource;
	this.apis = externalSource.getApis();
    }

    @PostLoad
    public void postLoad() {
	display = externalSource.getDisplay();
	iconSrc = Files.mediaSrc(this);
    }

    public int getId() {
	return id;
    }

    public ExternalSource getExternalSource() {
	return externalSource;
    }

    public void setApis(String apis) {
	this.apis = apis;
    }

    public String getApis() {
	return apis;
    }

    @Override
    public String getDomain() {
	return externalSource.getDomain();
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
	LinkSource other = (LinkSource) obj;
	if (id != other.id)
	    return false;
	return true;
    }
}
