package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "atlasobject")
public class AtlasObjectKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private AtlasCollectionKey collection;

    private String name;

    public AtlasCollectionKey getCollection() {
        return collection;
    }

    public void setCollection(AtlasCollectionKey collection) {
        this.collection = collection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
}
