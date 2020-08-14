package com.kidscademy.atlas.studio.export;

import com.kidscademy.atlas.studio.model.Feature;
import com.kidscademy.atlas.studio.model.FeatureValueFormat;
import com.kidscademy.atlas.studio.model.PhysicalQuantity;

import js.lang.Displayable;
import js.util.Strings;

public class ExportFeature implements Displayable
{
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
  public String toDisplay() {
    return Strings.concat(nameDisplay, ' ', valueDisplay);
  }

  @Override
  public String toString() {
    return Strings.toString(name, value, maximum, quantity);
  }
}
