package com.kidscademy.atlas.studio.export;

import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.FeatureValueFormat;
import com.kidscademy.atlas.studio.model.PhysicalQuantity;

import js.util.Strings;

@SuppressWarnings("unused")
public class ExportFeature {
    private String name;
    private double value;
    private Double maximum;
    private PhysicalQuantity quantity;

    private String nameDisplay;
    private String valueDisplay;

    public ExportFeature() {
    }

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
