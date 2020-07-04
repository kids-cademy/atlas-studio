package com.kidscademy.atlas.studio.export;

import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.FeatureValueFormat;
import com.kidscademy.atlas.studio.model.PhysicalQuantity;

import js.util.Strings;

@SuppressWarnings("unused")
public class ExportFeature {
    private final String name;
    private final double value;
    private final Double maximum;
    private final PhysicalQuantity quantity;

    private final String nameDisplay;
    private final String valueDisplay;

    public ExportFeature(Feature feature) {
	this.name = feature.getName();
	this.value = feature.getValue();
	this.maximum = feature.getMaximum();
	this.quantity = feature.getQuantity();

	this.nameDisplay = feature.getDisplay();
	this.valueDisplay = new FeatureValueFormat(feature).display();
    }

    @Override
    public String toString() {
	return Strings.toString(name, value, maximum, quantity);
    }
}
