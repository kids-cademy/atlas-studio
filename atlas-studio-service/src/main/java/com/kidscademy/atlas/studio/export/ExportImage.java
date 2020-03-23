package com.kidscademy.atlas.studio.export;

import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Image;

public class ExportImage {
    private final String path;
    private final String src;
    private final String caption;

    public ExportImage(AtlasObject object, Image image) {
	this.path = Util.path(object.getName(), image.getFileName());
	this.src = image.getSrc().value();

	String caption = image.getCaption();
	if (caption == null) {
	    this.caption = null;
	    return;
	}

	// TODO: HACK
	// <caption>...</caption>
	this.caption = caption.substring(9, caption.length() - 10);
    }

    public String getPath() {
	return path;
    }

    public String getSrc() {
	return src;
    }

    public String getCaption() {
	return caption;
    }
}
