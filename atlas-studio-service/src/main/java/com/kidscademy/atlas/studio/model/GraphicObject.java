package com.kidscademy.atlas.studio.model;

import java.util.Date;

/**
 * Object displayed on user interface.
 * 
 * @author Iulian Rotaru
 */
public interface GraphicObject {
    Date getTimestamp();
    
    String getName();
    
    String getDisplay();
    
    String getDefinition();
    
    MediaSRC getIconSrc();
}
