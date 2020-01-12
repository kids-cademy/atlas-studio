package com.kidscademy.atlas.studio.export;

import java.util.HashMap;
import java.util.Map;

import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.FeatureValueFormat;
import com.kidscademy.atlas.studio.model.PhysicalQuantity;

import js.lang.BugError;
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

	this.nameDisplay = nameDisplay(feature.getName());
	this.valueDisplay = new FeatureValueFormat(feature).display();
    }

    @Override
    public String toString() {
	return Strings.toString(name, value, maximum, quantity);
    }

    public static String nameDisplay(String name) {
	String display = EN.get(name);
	if (display == null) {
	    throw new BugError("Not mapped feature name |%s|.", name);
	}
	return display;
    }

    private static final Map<String, String> EN = new HashMap<>();
    static {
	EN.put("lifespan", "Lifespan");
	EN.put("lifespan.record", "Lifespan Record");
	EN.put("lifespan.captivity", "Lifespan in Captivity");
	EN.put("length", "Body Length");
	EN.put("length.bill", "Bill Length");
	EN.put("length.tail", "Tail Length");
	EN.put("length.claw", "Claws Size");
	EN.put("height", "Height");
	EN.put("height.shoulder", "Height at Shoulder");
	EN.put("height.hip", "Height at Hip");
	EN.put("wingspan", "Wingspan");
	EN.put("weight", "Weight");
	EN.put("speed", "Speed");
	EN.put("speed.full", "Full Speed");
	EN.put("speed.gliding", "Gliding Speed");
	EN.put("speed.diving", "Diving Speed");
	EN.put("diving.depth", "Diving Depth");
	EN.put("altitude", "Altitude");
    }
}
