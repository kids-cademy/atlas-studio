package com.kidscademy.atlas.studio.tool;

import java.io.File;
import java.net.URL;

public class ExternalSource {
    private final String name;
    private final URL url;
    private final File icon;

    public ExternalSource() {
	this.name = null;
	this.url = null;
	this.icon = null;
    }

    public ExternalSource(String name, URL url, File icon) {
	this.name = name;
	this.url = url;
	this.icon = icon;
    }

    public String getName() {
	return name;
    }

    public URL getUrl() {
	return url;
    }

    public File getIcon() {
	return icon;
    }
}
