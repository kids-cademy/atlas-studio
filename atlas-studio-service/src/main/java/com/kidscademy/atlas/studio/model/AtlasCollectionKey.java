package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "atlascollection")
public class AtlasCollectionKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public AtlasCollectionKey() {
    }

    public AtlasCollectionKey(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    @Override
    public String toString() {
	return "AtlasCollectionKey [id=" + id + "]";
    }
}
