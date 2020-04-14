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

import js.util.Strings;

@Entity
@Table(name = "\"RELEASE\"")
public class ReleaseItem implements GraphicObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Transient
    private final String title = "Release";

    private Date timestamp;
    private String name;
    private String display;
    private String definition;

    @Transient
    private MediaSRC iconSrc;

    @PostLoad
    public void postLoad() throws IOException {
	iconSrc = new MediaSRC(Strings.concat("/media/release/", name, "/icon.png"));
    }

    public int getId() {
	return id;
    }

    @Override
    public Date getTimestamp() {
	return timestamp;
    }

    @Override
    public String getTitle() {
	return title;
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
