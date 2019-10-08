package com.kidscademy.atlas.studio.export;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasObject;

@Entity
public class ExportItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    private AtlasCollection collection;

    @Transient
    private int index;
    
    private String name;
    private String display;
    private AtlasObject.State state;
    
    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
	return name;
    }

    public String getDisplay() {
        return display;
    }

    public AtlasObject.State getState() {
        return state;
    }
}
