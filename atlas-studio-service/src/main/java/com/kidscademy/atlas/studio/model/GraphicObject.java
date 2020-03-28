package com.kidscademy.atlas.studio.model;

import java.util.Date;

/**
 * Object displayed on user interface.
 * 
 * @author Iulian Rotaru
 */
public interface GraphicObject {
    Date getLastUpdated();
    
    String getName();
    
    String getDisplay();
    
    String getDefinition();
    
    MediaSRC getIconSrc();
}
