package com.kidscademy.atlas.studio.export;

import com.kidscademy.atlas.studio.model.AtlasItem;

import js.lang.Displayable;

@SuppressWarnings("unused")
public class ExportRelatedObject implements Displayable {
    private final int index;
    private final String display;
    private final String definition;
    private final String iconPath;
    private final String iconSrc;

    public ExportRelatedObject(ExportItem item) {
	this.index = item.getIndex();
	this.display = item.getDisplay();
	this.definition = item.getDefinition();
	this.iconPath = item.getIconPath();
	this.iconSrc = null;
    }

    public ExportRelatedObject(AtlasItem item) {
	// this constructor is used for atlas object preview and for now does not use
	// index value
	this.index = 0;

	this.display = item.getDisplay();
	this.definition = item.getDefinition();
	this.iconPath = Util.path(item.getName(), item.getIconName());
	if (item.getIconSrc() != null) {
	    this.iconSrc = item.getIconSrc().value();
	} else {
	    this.iconSrc = null;
	}
    }

    @Override
    public String toDisplay() {
	return display;
    }
}
