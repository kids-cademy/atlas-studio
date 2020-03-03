package com.kidscademy.atlas.studio.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class ObjectMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String definition;
    @Transient
    private List<TaxonMeta> taxonomyMeta;
    private TaxonomyClass taxonomyClass;
    private FeaturesClass featuresClass;
    
    @Transient
    private boolean endDate;
    @Transient
    private boolean conservationStatus;
    @Transient
    private boolean audioSample;

    public ObjectMeta() {
	this.taxonomyMeta = new ArrayList<>();
	this.taxonomyMeta.add(new TaxonMeta("family", "KEYBOARD,PERCUSSION,WOODWIND,BRASS,STRINGS,LAMELLOPHONE"));
	this.taxonomyMeta.add(new TaxonMeta("class", null));
    }

    public ObjectMeta(int id, String name) {
	this.id = id;
	this.name = name;
	this.definition = name;
    }

    public int getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public String getDefinition() {
	return definition;
    }

    public TaxonomyClass getTaxonomyClass() {
	return taxonomyClass;
    }

    public FeaturesClass getFeaturesClass() {
	return featuresClass;
    }
}
