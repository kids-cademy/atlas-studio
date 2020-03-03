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

    public boolean isEndDate() {
	return endDate;
    }

    public boolean isConservationStatus() {
	return conservationStatus;
    }

    public boolean isAudioSample() {
	return audioSample;
    }
}
