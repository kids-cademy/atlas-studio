package com.kidscademy.atlas.studio.model;

import js.lang.Displayable;

public enum ConservationStatus implements Displayable {
    /**
     * Least Concern. Lowest risk; does not qualify for a higher risk category.
     * Widespread and abundant taxa are included in this category.
     */
    LC("Least Concern"),
    /** Near Threatened. Likely to become endangered in the near future. */
    NT("Near Threatened"),
    /** Vulnerable. High risk of endangerment in the wild. */
    VU("Vulnerable"),
    /** Endangered. High risk of extinction in the wild. */
    EN("Endangered"),
    /** Critically Endangered. Extremely high risk of extinction in the wild. */
    CR("Critically Endangered"),
    /**
     * Extinct in the Wild. Known only to survive in captivity, or as a naturalized
     * population outside its historic range.
     */
    EW("Extinct in the Wild"),
    /** Extinct. No known individuals remaining. */
    EX("Extinct"),
    /**
     * Not Available. Not enough data to make an assessment of its risk of
     * extinction or species has not yet been evaluated against the criteria.
     */
    NA("Not Available");

    private String display;

    private ConservationStatus(String display) {
	this.display = display;
    }

    @Override
    public String toDisplay() {
	return display;
    }

    public boolean isLowRisk() {
	return this == LC || this == NT;
    }

    public boolean isThreatened() {
	return this == VU || this == EN || this == CR;
    }

    public boolean isExtinct() {
	return this == EW || this == EX;
    }
}
