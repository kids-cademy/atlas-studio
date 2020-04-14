package com.kidscademy.atlas.studio.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

@Entity
public class AndroidApp implements GraphicObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date timestamp;
    @Transient
    private final String title = "Android App";
    @Transient
    private String name;
    @Transient
    private String display;
    @Transient
    private String definition;
    @Transient
    private MediaSRC iconSrc;

    @OneToOne
    private Release release;

    private String packageName;
    private int versionCode;
    private URL gitRepository;
    private String gitUserName;
    private String gitPassword;

    @PostLoad
    public void postLoad() throws IOException {
	name = release.getName();
	display = release.getDisplay();
	definition = release.getDefinition();
	iconSrc = release.getIconSrc();
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

    public Release getRelease() {
	return release;
    }

    public String getPackageName() {
	return packageName;
    }

    public int getVersionCode() {
	return versionCode;
    }

    public URL getGitRepository() {
	return gitRepository;
    }

    public String getGitUserName() {
	return gitUserName;
    }

    public String getGitPassword() {
	return gitPassword;
    }

    public File getDir() {
	return AndroidProject.appDir(name);
    }

    public static AndroidApp create(Release release) {
	AndroidApp app = new AndroidApp();
	app.timestamp = new Date();
	app.release = release;
	app.name = release.getName();
	app.display = release.getDisplay();
	app.definition = release.getDefinition();
	app.iconSrc = release.getIconSrc();
	app.versionCode = 1;
	return app;
    }
}
