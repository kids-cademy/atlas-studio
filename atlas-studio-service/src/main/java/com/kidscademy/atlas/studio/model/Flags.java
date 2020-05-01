package com.kidscademy.atlas.studio.model;

import javax.persistence.Embeddable;

@Embeddable
public class Flags {
    /**
     * Flag for object end date, default to true. If this flag is false user
     * interface should not display controls for end date.
     */
    private boolean endDate;
    /**
     * Flag for object progenitor, default to true. If this flag is false user
     * interface should not display progenitor control.
     */
    private boolean progenitor;
    /**
     * Flag for object conservation status, default to true. If this flag is false
     * user interface should not display controls for conservation status.
     */
    private boolean conservationStatus;
    /**
     * Flag for object audio sample, default to true. If this flag is false user
     * interface should not display controls for audio sample.
     */
    private boolean audioSample;
    /**
     * Flag for object spreading property, default to true. If this flag is false
     * user interface should not display controls for object spreading.
     */
    private boolean spreading;

    public Flags() {

    }

    public Flags(boolean defaultValue) {
	endDate = defaultValue;
	progenitor = defaultValue;
	conservationStatus = defaultValue;
	audioSample = defaultValue;
	spreading = defaultValue;
    }

    public void setEndDate(boolean endDate) {
	this.endDate = endDate;
    }

    public boolean hasEndDate() {
	return endDate;
    }

    public boolean hasProgenitor() {
	return progenitor;
    }

    public void setProgenitor(boolean progenitor) {
	this.progenitor = progenitor;
    }

    public void setConservationStatus(boolean conservationStatus) {
	this.conservationStatus = conservationStatus;
    }

    public boolean hasConservationStatus() {
	return conservationStatus;
    }

    public void setAudioSample(boolean audioSample) {
	this.audioSample = audioSample;
    }

    public boolean hasAudioSample() {
	return audioSample;
    }

    public void setSpreading(boolean spreading) {
	this.spreading = spreading;
    }

    public boolean hasSpreading() {
	return spreading;
    }
}
