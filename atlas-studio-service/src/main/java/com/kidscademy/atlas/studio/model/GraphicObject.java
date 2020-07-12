package com.kidscademy.atlas.studio.model;

/**
 * Object displayed on user interface.
 * 
 * @author Iulian Rotaru
 */
public interface GraphicObject extends Icon {
    String getTitle();
    
    String getName();

    String getDefinition();
}
