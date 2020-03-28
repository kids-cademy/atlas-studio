package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "atlasobject")
public class ReleaseChild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public ReleaseChild() {
    }

    public ReleaseChild(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
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
	ReleaseChild other = (ReleaseChild) obj;
	if (id != other.id)
	    return false;
	return true;
    }
}
