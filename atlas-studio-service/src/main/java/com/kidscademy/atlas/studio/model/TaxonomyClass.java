package com.kidscademy.atlas.studio.model;

/**
 * The class of taxonomy defines classification type and is used to select the
 * user interface layout used by this control. Current supported values are
 * 'MUSICAL_INSTRUMENT' and 'BIOLOGICAL'.
 * 
 * Musical instrument taxonomy has only one classification criterion named
 * 'Family' and accepts values from an enumeration.
 * 
 * Biological taxonomy is the scientific classification for animals and plants.
 * 
 * @author Iulian Rotaru
 */
public enum TaxonomyClass {
    MUSICAL_INSTRUMENT, BIOLOGICAL;
}
