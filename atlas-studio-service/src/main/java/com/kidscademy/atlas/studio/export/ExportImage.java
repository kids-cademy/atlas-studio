package com.kidscademy.atlas.studio.export;

import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Image;

public class ExportImage {
    private final String path;
    private final String src;
    private final String caption;
    private final String style;

    public ExportImage(AtlasObject object, Image image) {
	this.path = Util.path(object.getName(), image.getFileName());
	this.src = image.getSrc().value();

	String caption = image.getCaption();
	if (caption == null) {
	    this.caption = null;
	} else {
	    // TODO: HACK
	    // <caption>...</caption>
	    this.caption = caption.substring(9, caption.length() - 10);
	}

	double ratio = (double)image.getWidth() / (double)image.getHeight();
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

    public String getCaption() {
	return caption;
    }

    public String getStyle() {
	return style;
    }
}
