package com.kidscademy.atlas.studio.tool;

import java.net.URL;

import javax.persistence.Embeddable;

@Embeddable
class Link {
    URL url;
    String domain;
    String display;
    String description;
    String iconName;
    String features;

    public Link() {
    }

    public Link(URL url, String display, String description, String iconName) {
	this.url = url;
	this.display = display;
	this.description = description;
	this.iconName = iconName;
    }

    public URL getUrl() {
	return url;
    }

    public String getDomain() {
	return domain;
    }

    public String getDisplay() {
	return display;
    }

    public String getDescription() {
	return description;
    }

    public String getIconName() {
	return iconName;
    }

    public String getFeatures() {
	return features;
    }
}
