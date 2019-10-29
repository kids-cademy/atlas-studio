package com.kidscademy.atlas.studio.export;

import java.net.URL;

import com.kidscademy.atlas.studio.model.Link;

import js.util.Strings;

@SuppressWarnings("unused")
public class ExportLink {
    private final URL url;
    private final String display;
    private final String description;
    private final String iconPath;

    public ExportLink(Link link) {
	this.url = link.getUrl();
	this.display = link.toDisplay();
	this.description = link.getDescription();
	this.iconPath = Strings.concat("link/", link.getIconName());
    }
}
