package com.kidscademy.atlas.studio.export;

import java.util.ArrayList;
import java.util.List;

import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Image;

import js.dom.Document;
import js.dom.DocumentBuilder;
import js.dom.Element;
import js.util.Classes;

public class ExportImage {
    private static final DocumentBuilder documentBuilder = Classes.loadService(DocumentBuilder.class);

    private String path;
    private String src;
    private List<String> caption;
    private String style;

    public ExportImage() {
    }

    public ExportImage(AtlasObject object, Image image) {
	this.path = Util.path(object.getName(), image.getFileName());
	this.src = image.getSrc().value();

	String caption = image.getCaption();
	if (caption == null) {
	    this.caption = null;
	} else {
	    Document document = documentBuilder.parseXML(caption);
	    this.caption = new ArrayList<>();
	    for (Element paragraph : document.findByTag("p")) {
		this.caption.add(paragraph.getText());
	    }
	}

	double ratio = (double) image.getWidth() / (double) image.getHeight();
	if (ratio < 0.75) {
	    style = "narrow-image";
	} else if (ratio < 1.1) {
	    style = "square-image";
	} else {
	    style = "wide-image";
	}
    }

    public String getPath() {
	return path;
    }

    public String getSrc() {
	return src;
    }

    public List<String> getCaption() {
	return caption;
    }

    public String getStyle() {
	return style;
    }
}
