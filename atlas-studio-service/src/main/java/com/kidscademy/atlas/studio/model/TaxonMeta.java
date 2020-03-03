package com.kidscademy.atlas.studio.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TaxonMeta {
    private String name;

    /**
     * Comma separated values allowed for this taxon or null if input is not
     * constrained. User interface should use appropriate widgets: a select for list
     * of values or free text input if this allowed values list is null.
     */
    @Column(name = "\"VALUES\"")
    private String values;

    public TaxonMeta() {
    }

    public TaxonMeta(String name, String values) {
	this.name = name;
	this.values = values;
    }

    public String getName() {
	return name;
    }

    public String getValues() {
	return values;
    }
}
