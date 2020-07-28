package com.kidscademy.atlas.studio.model;

import js.util.Strings;

public enum Theme {
    // ENUM('CLASSIC','MODERN')
    CLASSIC, MODERN;

    /**
     * Get theme value using CSS style name convention, that is, all lower case.
     * 
     * @return CSS style value.
     */
    public String cssStyle() {
	return this.name().toLowerCase();
    }

    /**
     * Get theme value using Android theme name convention, that is, title case.
     * 
     * @return Android theme value.
     */
    public String androidTheme() {
	return Strings.toTitleCase(this.name());
    }
}
