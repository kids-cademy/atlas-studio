package com.kidscademy.atlas.studio.export;

import js.lang.Displayable;

@SuppressWarnings("unused")
public class ExportRelatedObject implements Displayable {
    private final int index;
    private final String display;
    private final String definition;
    private final String iconPath;

    public ExportRelatedObject(ExportItem item) {
	this.index = item.getIndex();
	this.display = item.getDisplay();
	this.definition = item.getDefinition();
	this.iconPath = item.getIconPath();
    }

    @Override
    public String toDisplay() {
        return display;
    }
}
