package com.kidscademy.atlas.studio.export;

import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Image;

@SuppressWarnings("unused")
public class ExportImage {
    private final String path;
    private final String caption;

    public ExportImage(AtlasObject object, Image image) {
	this.path = Util.path(object.getName(), image.getFileName());
	this.caption = image.getCaption();
    }

    public String getPath() {
	return path;
    }
}
