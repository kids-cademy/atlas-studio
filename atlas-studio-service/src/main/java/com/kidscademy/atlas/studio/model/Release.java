package com.kidscademy.atlas.studio.model;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.Application;

import js.util.Strings;

@Entity
@Table(name = "\"RELEASE\"")
public class Release implements GraphicObject {
    @Transient
    private transient final Application application;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date lastUpdated;
    private String name;
    private String display;
    private String packageName;

    @Transient
    private String definition;

    @Transient
    private MediaSRC iconSrc;

    @Transient
    private Map<String, String> properties;

    @Transient
    private Map<String, MediaSRC> images;

    public Release() {
	this.application = Application.instance();
    }

    /**
     * Test constructor.
     * 
     * @param application
     *            application mock.
     */
    public Release(Application application) {
	this.application = application;
    }

    @PostLoad
    public void postLoad() throws IOException {
	iconSrc = new MediaSRC(Strings.concat("/media/release/", name, "/icon.png"));

	images = new HashMap<>();
	images.put("icon", new MediaSRC(Strings.concat("/media/release/", name, "/icon.png")));
	images.put("feature", new MediaSRC(Strings.concat("/media/release/", name, "/feature.png")));
	images.put("cover", new MediaSRC(Strings.concat("/media/release/", name, "/cover.png")));

	Project project = new Project(application, name);
	properties = project.getProprties();
	definition = properties.get("app_about");
    }

    @PrePersist
    public void prePersist() throws IOException {
	postMerge(this);
    }

    public void postMerge(Release release) throws IOException {
	if (release.properties == null) {
	    Project project = new Project(application, name);
	    project.setProperties(properties);
	}
    }

    public int getId() {
	return id;
    }

    public void setLastUpdated(Date lastUpdated) {
	this.lastUpdated = lastUpdated;
    }

    @Override
    public Date getLastUpdated() {
	return lastUpdated;
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public String getDisplay() {
	return display;
    }

    @Override
    public String getDefinition() {
	return definition;
    }

    @Override
    public MediaSRC getIconSrc() {
	return iconSrc;
    }

    public String getPackageName() {
	return packageName;
    }

    public Map<String, String> getProperties() {
	return properties;
    }
}
