package com.kidscademy.atlas.studio.model;

import javax.persistence.Embeddable;

@Embeddable
public class Related {
    private String name;
    private float relevance;

    public Related() {
    }

    /**
     * Test constructor.
     * 
     * @param name
     * @param relevance
     */
    public Related(String name, float relevance) {
	this.name = name;
	this.relevance = relevance;
    }

    public String getName() {
	return name;
    }

    public float getRelevance() {
	return relevance;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
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
	Related other = (Related) obj;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return name;
    }
}
