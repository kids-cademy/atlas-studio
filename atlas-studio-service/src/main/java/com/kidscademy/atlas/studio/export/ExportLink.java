package com.kidscademy.atlas.studio.export;

import java.net.URL;

import com.kidscademy.atlas.studio.model.Link;
import com.kidscademy.atlas.studio.util.Files;

import js.util.Strings;

@SuppressWarnings("unused")
public class ExportLink {
    private URL url;
    private String display;
    private String description;
    private String iconPath;
    private String iconSrc;

    public ExportLink() {
    }

    public ExportLink(Link link) {
	this.url = link.getUrl();
	this.display = link.toDisplay();
	this.description = link.getDefinition();
	this.iconPath = Strings.concat("link/", Files.basename(link.getDomain()), ".png");
	this.iconSrc = link.getIconSrc().value();
    }
}
