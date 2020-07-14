package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import js.tiny.container.annotation.TestConstructor;

@Entity
@Table(name = "atlascollection")
public class AtlasCollectionKey implements Key {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String name;
    
    public AtlasCollectionKey() {
    }

    @TestConstructor
    public AtlasCollectionKey(int id, String name) {
	this.id = id;
	this.name = name;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
	return "AtlasCollectionKey [id=" + id + ", name=" + name + "]";
    }
}
