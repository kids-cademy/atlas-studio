package com.kidscademy.atlas.studio.export;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.RepositoryObject;

@Entity
@Table(name="atlasitem")
public class ExportItem implements RepositoryObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    private AtlasCollection collection;
    
    private String name;
    private String display;
    private String definition;
    private String iconName;
    private AtlasObject.State state;


    @Transient
    private int index;
    @Transient
    private String iconPath;
    
    public int getId() {
        return id;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getIconPath() {
        return iconPath;
    }

    @Override
    public String getRepositoryName() {
	return collection.getName();
    }

    @Override
    public String getName() {
	return name;
    }

    public String getDisplay() {
        return display;
    }

    public String getDefinition() {
        return definition;
    }

    public String getIconName() {
        return iconName;
    }

    public AtlasObject.State getState() {
        return state;
    }
}
