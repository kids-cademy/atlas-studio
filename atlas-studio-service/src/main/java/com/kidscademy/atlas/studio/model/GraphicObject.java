package com.kidscademy.atlas.studio.model;

import java.util.Date;

/**
 * Object displayed on user interface.
 * 
 * @author Iulian Rotaru
 */
public interface GraphicObject {
    Date getTimestamp();

    String getTitle();
    
    String getName();

    String getDisplay();

    /**
     * Optional short description, null if not used.
     * 
     * @return
     */
    String getDefinition();

    MediaSRC getIconSrc();
}
