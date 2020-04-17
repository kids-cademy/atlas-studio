package com.kidscademy.atlas.studio.model;

public class FeatureValueFormat {
    private final String display;

    public FeatureValueFormat(Feature feature) {
	if (feature.isScalar()) {
	    if (feature.hasMaximum()) {
		QuantityFormat maximum = new QuantityFormat(feature.getMaximum(), feature.getQuantity());
		if (feature.getValue() == 0) {
		    this.display = String.format("Up to %s", maximum.value());
		} else {
		    QuantityFormat minimum = new QuantityFormat(feature.getValue(), feature.getQuantity());
		    this.display = String.format("%s to %s", minimum.value(), maximum.value());
		}
	    } else {
		QuantityFormat quantity = new QuantityFormat(feature.getValue(), feature.getQuantity());
		this.display = quantity.value();
	    }
	    return;
	}

	if (feature.hasMaximum()) {
	    QuantityFormat maximum = new QuantityFormat(feature.getMaximum(), feature.getQuantity());
	    if (feature.getValue() == 0) {
		this.display = String.format("Up to %s %s", maximum.value(), maximum.units());
	    } else {
		QuantityFormat minimum = new QuantityFormat(feature.getValue(), feature.getQuantity());
		if (minimum.units().equals(maximum.units())) {
		    this.display = String.format("%s to %s %s", minimum.value(), maximum.value(), maximum.units());
		} else {
		    this.display = String.format("%s %s to %s %s", minimum.value(), minimum.units(), maximum.value(),
			    maximum.units());
		}
	    }
	} else {
	    QuantityFormat quantity = new QuantityFormat(feature.getValue(), feature.getQuantity());
	    this.display = String.format("%s %s", quantity.value(), quantity.units());
	}
    }

    public String display() {
	return display;
    }
}
