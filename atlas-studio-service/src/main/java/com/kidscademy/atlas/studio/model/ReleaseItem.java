package com.kidscademy.atlas.studio.model;

import java.io.IOException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.kidscademy.atlas.studio.Application;

import js.util.Strings;

@Entity
@Table(name = "\"RELEASE\"")
public class ReleaseItem implements GraphicObject {
    @Transient
    private transient final Application application;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date lastUpdated;
    private String name;
    private String display;

    @Transient
    private String definition;

    @Transient
    private MediaSRC iconSrc;

    public ReleaseItem() {
	this.application = Application.instance();
    }

    /**
     * Test constructor.
     * 
     * @param application
     *            application mock.
     */
    public ReleaseItem(Application application) {
	this.application = application;
    }

    @PostLoad
    public void postLoad() throws IOException {
	Project project = new Project(application, name);
	definition = project.getValue("app_about");
	iconSrc = new MediaSRC(Strings.concat("/media/release/", name, "/icon.png"));
    }

    public int getId() {
	return id;
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
}
