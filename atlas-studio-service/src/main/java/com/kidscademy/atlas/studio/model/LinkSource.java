package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.util.Files;

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
}
