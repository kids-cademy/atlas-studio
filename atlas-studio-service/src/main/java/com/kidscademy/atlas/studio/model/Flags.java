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
     * Flag for object spreading property, default to true. If this flag is false user
     * interface should not display controls for object spreading.
     */
    private boolean spreading;
    
    public void setEndDate(boolean endDate) {
        this.endDate = endDate;
    }

    public boolean isEndDate() {
	return endDate;
    }

    public void setConservationStatus(boolean conservationStatus) {
        this.conservationStatus = conservationStatus;
    }

    public boolean isConservationStatus() {
	return conservationStatus;
    }

    public void setAudioSample(boolean audioSample) {
        this.audioSample = audioSample;
    }

    public boolean isAudioSample() {
	return audioSample;
    }

    public void setSpreading(boolean spreading) {
        this.spreading = spreading;
    }

    public boolean isSpreading() {
        return spreading;
    }
}
